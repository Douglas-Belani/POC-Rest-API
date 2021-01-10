package services.impl;

import dao.IAuxRateUserDao;
import dao.IRateDao;
import dao.impl.AuxRateUserDao;
import dao.impl.RateDaoImpl;
import entities.Rate;
import resources.exception.ResourceNotFoundException;
import services.IRateService;
import java.sql.Connection;
import java.sql.SQLException;

public class RateServiceImpl implements IRateService {

    private Connection conn;
    private IAuxRateUserDao auxRateUserDao;
    private IRateDao rateDao;

    public RateServiceImpl(Connection conn) {
        this(new RateDaoImpl(conn), new AuxRateUserDao(conn));
        this.conn = conn;
    }

    public RateServiceImpl(IRateDao rateDao, IAuxRateUserDao auxRateUserDao) {
        this.rateDao = rateDao;
        this.auxRateUserDao = auxRateUserDao;
    }

    public RateServiceImpl(Connection conn, IRateDao rateDao, IAuxRateUserDao auxRateUserDao) {
        this.conn = conn;
        this.rateDao = rateDao;
        this.auxRateUserDao = auxRateUserDao;
    }


    @Override
    public Rate getRateById(Integer id) {
        Rate rate = rateDao.getRateById(id);

        if (rate == null) {
            throw new ResourceNotFoundException("Rate id " + id + " not found");
        }

        return rate;
    }

    @Override
    public boolean upvote(Integer userId, Integer rateId) throws SQLException {
        try {
            conn.setAutoCommit(false);

            int voteCode = auxRateUserDao.isAlreadyVoted(userId, rateId);
            if (voteCode == 0) {
                boolean upvoted = upvote(rateId);
                if (!upvoted) {
                    return false;
                }

                boolean insertedRateProduct = auxRateUserDao.insertUpvote(userId, rateId);
                if (!insertedRateProduct) {
                    conn.rollback();
                    return false;
                }

            } else if (voteCode == 1){
                Rate rate = rateDao.getRateById(rateId);
                if (rate == null) {
                    return false;

                }
                rate.decreaseUpvote();
                boolean updateRate = rateDao.update(rate);
                boolean deleted = auxRateUserDao.delete(userId, rateId);
                if (!(updateRate && deleted)) {
                    conn.rollback();
                    return false;
                }

            } else {
                Rate rate = rateDao.getRateById(rateId);
                if (rate == null) {
                    return false;
                }

                rate.decreaseDownvote();
                rate.addUpvote();
                boolean updateRate = rateDao.update(rate);
                boolean switchedVotes = auxRateUserDao.switchVotes(userId, rateId, voteCode);
                if (!(updateRate && switchedVotes)) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            return false;

        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean downvote(Integer userId, Integer rateId) throws SQLException {
        try {
            conn.setAutoCommit(false);

            int voteCode = auxRateUserDao.isAlreadyVoted(userId, rateId);
            if (voteCode == 0) {
                boolean downvoted = downvote(rateId);
                if (!downvoted) {
                    return false;
                }
                boolean insertedRateProduct = auxRateUserDao.insertDownvote(userId, rateId);

                if (!insertedRateProduct) {
                    conn.rollback();
                    return false;
                }

            } else if (voteCode == -1) {
                Rate rate = rateDao.getRateById(rateId);
                if (rate == null) {
                    return false;
                }
                rate.decreaseDownvote();
                boolean decreasedDownvote = rateDao.update(rate);
                boolean deleted = auxRateUserDao.delete(userId, rateId);

                if (!(decreasedDownvote && deleted)) {
                    conn.rollback();
                    return false;
                }

            } else {
                Rate rate = rateDao.getRateById(rateId);
                if (rate == null) {
                    return false;
                }

                rate.decreaseUpvote();
                rate.addDownvote();
                boolean updatedRate = rateDao.update(rate);
                boolean switched = auxRateUserDao.switchVotes(userId, rateId, voteCode);

                if (!(updatedRate && switched)) {
                    conn.rollback();
                    return false;
                }

            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            return false;

        } finally {
            conn.setAutoCommit(true);
        }
    }

    private boolean upvote(Integer id) {
        Rate rate = getRateById(id);
        if (rate != null) {
            rate.addUpvote();
            return rateDao.update(rate);

        } else {
            return false;
        }
    }


    private boolean downvote(Integer id) {
        Rate rate = getRateById(id);
        if (rate != null) {
            rate.addDownvote();
            return rateDao.update(rate);

        } else {
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        getRateById(id);
        auxRateUserDao.deleteByRateId(id);
        return rateDao.deleteRateById(id);
    }

    @Override
    public Rate createRate() {
        Rate rate = new Rate(null, 0, 0);
        Integer rateId = rateDao.insert(rate);
        rate.setRateId(rateId);

        return rate;
    }
}
