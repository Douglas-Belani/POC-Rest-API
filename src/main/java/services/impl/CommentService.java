package services.impl;

import dao.ICommentDao;
import dao.impl.CommentDaoImpl;
import entities.*;
import resources.exception.ResourceNotFoundException;
import services.ICommentService;
import services.IRateService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class CommentService implements ICommentService {

    private Connection conn;
    private ICommentDao commentDao;
    private IRateService rateService;

    public CommentService(Connection conn) {
        this(new CommentDaoImpl(conn), new RateServiceImpl(conn));
        this.conn = conn;
    }

    public CommentService(ICommentDao commentDao, IRateService rateService) {
        this.commentDao = commentDao;
        this.rateService = rateService;
    }

    public CommentService(Connection conn, ICommentDao commentDao, IRateService rateService) {
        this.conn = conn;
        this.commentDao = commentDao;
        this.rateService = rateService;
    }

    @Override
    public Integer comment(String text, User user, Integer productId, Integer topLevelCommentId)
            throws SQLException {
        Comments comments;
        if (topLevelCommentId == null) {
            comments = new TopLevelComment();

        } else {
            comments = new SubLevelComment();
            ((SubLevelComment)comments).setTopLevelCommentId(topLevelCommentId);
        }

        comments.setText(text);
        comments.setUser(user);

        try {
            conn.setAutoCommit(false);
            Rate rate = rateService.createRate();

            if (rate.getRateId() == null) {
                throw new SQLException();
            }

            comments.setRate(rate);
            Integer commentId = commentDao.insertComment(comments, productId);

            if (commentId == null) {
                throw new SQLException();
            }

            conn.commit();
            return commentId;

        } catch (SQLException e) {
            conn.rollback();
            return null;

        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean editComment(User user, String text, Integer commentId) {
        Comments comments = commentDao.getCommentById(commentId);
        if (comments == null) {
            throw new ResourceNotFoundException("Comment id " + commentId + " not found");
        }

        if (!comments.getUser().equals(user)) {
            throw new UnauthorizedException("Can't modify another user's comment.");
        }

        comments.setText(text);
        return commentDao.updateComment(comments);
    }

    @Override
    public boolean deleteComment(User user, Integer id) throws SQLException {
        Comments comments = commentDao.getCommentById(id);

        if (comments == null) {
            throw new ResourceNotFoundException("Comment id " + id + " not found");
        }

        if (!comments.getUser().equals(user)) {
            throw new UnauthorizedException("Can't delete another user's comment.");
        }

        try {
            conn.setAutoCommit(false);

            boolean deletedComment = commentDao.deleteCommentById(id);
            if (!deletedComment) {
                conn.rollback();
                return false;
            }

            if (comments instanceof TopLevelComment) {
                for (SubLevelComment subLevelComment : ((TopLevelComment) comments).getSubLevelComments()) {
                    boolean deletedRate = rateService.deleteById(subLevelComment.getRate().getRateId());

                    if (!deletedRate) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            boolean deletedRate = rateService.deleteById(comments.getRate().getRateId());

            if (!deletedRate) {
                conn.rollback();
                return false;
            }


            conn.commit();
            return true;

        } catch (SQLException | UnauthorizedException e) {
            throw e;

        } finally {
            conn.setAutoCommit(true);
        }
    }
}
