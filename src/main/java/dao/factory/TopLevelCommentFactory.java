package dao.factory;

import entities.Rate;
import entities.TopLevelComment;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TopLevelCommentFactory {

    public static TopLevelComment getTopLevelCommentFromResultSet(ResultSet rs, User user, Rate rate) throws SQLException {
        return new TopLevelComment(rs.getInt("commentId"), rs.getString("text"), user, rate);
    }
}
