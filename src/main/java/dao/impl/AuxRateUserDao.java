package dao.impl;

import dao.IAuxRateUserDao;
import dao.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuxRateUserDao implements IAuxRateUserDao {

    private Connection conn;

    public AuxRateUserDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int isAlreadyVoted(int userId, int rateId) throws SQLException {
       PreparedStatement ps = null;
       ResultSet rs = null;

       try {
           String query = "SELECT isUpvote, isDownvote " +
                          "FROM aux_rate_user " +
                          "WHERE userId = ? AND rateId = ?";

           ps = conn.prepareStatement(query);
           ps.setInt(1, userId);
           ps.setInt(2, rateId);

           rs = ps.executeQuery();

           if (!rs.next()) {
               return 0;

           } else if (rs.getBoolean("isUpvote")) {
               return 1;

           } else {
               return -1;
           }

       } catch (SQLException e) {
           e.printStackTrace();
           throw e;

       } finally {
           DatabaseConnection.closeResultSet(rs);
           DatabaseConnection.closePreparedStatement(ps);
       }
    }

    @Override
    public boolean insertUpvote(int userId, int rateId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "INSERT INTO aux_rate_user VALUES(?, ?, TRUE, FALSE)";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, userId);
            ps.setInt(2, rateId);

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
    public boolean insertDownvote(int userId, int rateId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "INSERT INTO aux_rate_user VALUES(?, ?, FALSE, TRUE)";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, userId);
            ps.setInt(2, rateId);

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
    public boolean switchVotes(int userId, int rateId, int voteCode) {
        PreparedStatement ps = null;
        try {

            String sqlCommand;
            if (voteCode == -1) {
                sqlCommand = "UPDATE aux_rate_user SET isUpvote = TRUE, isDownvote = FALSE " +
                             "WHERE userId = ? AND rateId = ?";

            } else {
                sqlCommand = "UPDATE aux_rate_user SET isUpvote = FALSE, isDownvote = TRUE " +
                             "WHERE userId = ? AND rateId = ?";
            }

            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, userId);
            ps.setInt(2, rateId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int userId, int rateId) {
        PreparedStatement ps = null;
        try {
            String sqlCommand = "DELETE FROM aux_rate_user WHERE userId = ? AND rateId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, userId);
            ps.setInt(2, rateId);

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
    public boolean deleteByRateId(int rateId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM aux_rate_user WHERE rateId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, rateId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected >= 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
