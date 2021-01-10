package dao.factory;

import entities.Rate;
import entities.SubLevelComment;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubLevelCommentFactory {

    public static SubLevelComment getSubLevelCommentFactoryFromResultSet(ResultSet rs, User user, Rate rate) throws SQLException {
        return new SubLevelComment(rs.getInt("commentId"), rs.getString("text"), user, rate,
                rs.getInt("topLevelCommentId"));
    }

}
