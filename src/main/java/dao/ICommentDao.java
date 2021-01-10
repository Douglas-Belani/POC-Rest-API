package dao;

import entities.Comments;

import java.util.List;

public interface ICommentDao {

    public abstract List<Comments> getAllCommentsByProductId(int productId);

    public abstract Comments getCommentById(int id);

    public abstract Integer insertComment(Comments comment, int productId);

    public abstract boolean updateComment(Comments comment);

    public abstract boolean deleteCommentById(int id);

}
