package dao;

public interface IAuxRateCommentDao {

    public abstract boolean isAlreadyVoted(int userId, int commentId);

    public abstract boolean insertUpvotedComment(int userId, int commentId);

    public abstract boolean insertDownvotedComment(int userId, int commentId);

    public abstract boolean deleteVotedComment(int userId, int commentId);

    public abstract boolean deleteVotedCommentById(int commentId);

}
