package dao.impl;

import dao.IRateDao;
import dao.connection.DatabaseConnection;
import dao.factory.RateFactory;
import entities.Rate;

import java.sql.*;

public class RateDaoImpl implements IRateDao {

    private Connection conn;

    public RateDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Rate getRateById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Rate rate = null;

        try {
            String query = "SELECT `r`.rateId, `r`.upvotes, `r`.downvotes " +
                           "FROM rate `r` " +
                           "WHERE `r`.rateId = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                rate = RateFactory.getRateFromResultSetRs(rs);
            }

            return rate;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insert(Rate rate) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO rate VALUES(NULL, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, rate.getUpvotes());
            ps.setInt(2, rate.getDownvotes());
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
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean update(Rate rate) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE rate SET rate.upvotes = ?, rate.downvotes = ? WHERE rateId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, rate.getUpvotes());
            ps.setInt(2, rate.getDownvotes());
            ps.setInt(3, rate.getRateId());

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
    public boolean deleteRateById(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM rate WHERE rateId = ?";
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
