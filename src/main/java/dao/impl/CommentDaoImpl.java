package dao.impl;

import dao.ICommentDao;
import dao.connection.DatabaseConnection;
import dao.factory.*;
import entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements ICommentDao {

    private Connection conn;

    public CommentDaoImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public List<Comments> getAllCommentsByProductId(int productId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Comments> comments = new ArrayList<>();

        try {
            String query = "SELECT `co`.commentId, `co`.text, `co`.topLevelCommentId, " +
                           "`u`.userId, `u`.userName, `u`.cpf, `u`.email, `u`.password, `u`.birthDate, " +
                           "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                           "FROM comments `co` " +
                           "INNER JOIN user `u` ON `co`.userId = `u`.userId " +
                           "INNER JOIN rate `r` ON `co`.rateId = `r`.rateId " +
                           "WHERE `co`.productId = ? " +
                           "ORDER BY `co`.topLevelCommentId, `co`.commentId ";
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast() && rs.getInt("topLevelCommentId") == 0) {
                    User user = UserFactory.getUserFromResultSet(rs);
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    TopLevelComment topLevelComment = TopLevelCommentFactory
                            .getTopLevelCommentFromResultSet(rs, user, rate);
                    comments.add(topLevelComment);
                    rs.next();
                }

                while (!rs.isAfterLast()) {
                    User user = UserFactory.getUserFromResultSet(rs);
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    SubLevelComment subLevelComment = SubLevelCommentFactory
                            .getSubLevelCommentFactoryFromResultSet(rs, user, rate);

                    for (Comments topLevelComments : comments) {
                        if (subLevelComment.getTopLevelCommentId().equals(topLevelComments.getCommentId())) {
                            ((TopLevelComment) topLevelComments).addSubLevelComment(subLevelComment);
                        }
                    }
                    rs.next();
                }
            }
            return comments;

        } catch (SQLException e) {
            e.printStackTrace();
            return comments;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
            DatabaseConnection.closeResultSet(rs);
        }
    }

    @Override
    public Comments getCommentById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Comments comment = null;

        try {
            String query = "SELECT `co`.commentId, `co`.text, `co`.topLevelCommentId, " +
                           "`u`.userId, `u`.userName, `u`.cpf,`u`.email, `u`.password, `u`.birthDate, " +
                           "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                           "FROM comments `co` " +
                           "INNER JOIN user `u` ON `co`.userId = `u`.userId " +
                           "INNER JOIN rate `r` ON `co`.rateId = `r`.rateId " +
                           "WHERE `co`.commentId = ? OR `co`.topLevelCommentId = ? " +
                           "ORDER BY `co`.topLevelCommentId" ;
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Rate rate = RateFactory.getRateFromResultSetRs(rs);
                User user = UserFactory.getUserFromResultSet(rs);
                if (rs.getInt("topLevelCommentId") == 0) {
                    comment = TopLevelCommentFactory.getTopLevelCommentFromResultSet(rs, user, rate);
                    rs.next();
                    while(!rs.isAfterLast()) {
                        User subLevelCommentUser = UserFactory.getUserFromResultSet(rs);
                        Rate subLevelCommentRate = RateFactory.getRateFromResultSetRs(rs);
                        ((TopLevelComment) comment).addSubLevelComment(
                                SubLevelCommentFactory.getSubLevelCommentFactoryFromResultSet(rs, subLevelCommentUser,
                                        subLevelCommentRate));
                        rs.next();
                    }

                } else {
                    comment = SubLevelCommentFactory.getSubLevelCommentFactoryFromResultSet(rs, user, rate);
                }
            }

            return comment;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertComment(Comments comment, int productId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO comments VALUES (NULL, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getText());
            ps.setInt(2, comment.getUser().getUserId());
            ps.setInt(3, productId);
            ps.setInt(4, comment.getRate().getRateId());

            if (comment instanceof TopLevelComment) {
                ps.setNull(5, Types.NULL);

            } else {
                ps.setInt(5, ((SubLevelComment) comment).getTopLevelCommentId());
            }
            ps.execute();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
            DatabaseConnection.closeResultSet(rs);
        }
    }

    @Override
    public boolean updateComment(Comments comment) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE comments " +
                                "SET text = ? " +
                                "WHERE commentId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setString(1, comment.getText());
            ps.setInt(2, comment.getCommentId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean deleteCommentById(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM comments WHERE topLevelCommentId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, id);

            ps.executeUpdate();

            sqlCommand = "DELETE FROM comments WHERE commentId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
