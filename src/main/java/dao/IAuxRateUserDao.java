package dao;

import java.sql.SQLException;

public interface IAuxRateUserDao {

    public abstract int isAlreadyVoted(int userId, int rateId) throws SQLException;

    public abstract boolean insertUpvote(int userId, int rateId);

    public abstract boolean insertDownvote(int userId, int rateId);

    public abstract boolean switchVotes(int userId, int rateId, int voteCode);

    public abstract boolean delete(int userId, int rateId);

    public abstract boolean deleteByRateId(int rateId);

}
