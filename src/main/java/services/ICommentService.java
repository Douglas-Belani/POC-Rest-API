package services;

import entities.User;

import java.sql.SQLException;

public interface ICommentService {

    public abstract Integer comment(String text, User user, Integer productId, Integer commentRepliedId) throws SQLException;

    public abstract boolean editComment(User user, String text, Integer commentId);

    public abstract boolean deleteComment(User user, Integer id) throws SQLException;

}
