package dao.impl;

import dao.IAuxProductCategoryDao;
import dao.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuxProductCategoryDaoImpl implements IAuxProductCategoryDao {

    private Connection conn;

    public AuxProductCategoryDaoImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean insert(int productId, int categoryId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "INSERT INTO aux_product_category VALUES(?, ?)";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, productId);
            ps.setInt(2, categoryId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean update(int productId, int oldCategoryId, int newCategoryId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE aux_product_category " +
                    "SET categoryId = ? WHERE productId = ? AND categoryId = ?" ;
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, newCategoryId);
            ps.setInt(2, productId);
            ps.setInt(3, oldCategoryId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean deleteByProductIdAndCategoryId(int productId, int categoryId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM aux_product_category WHERE productId = ? AND categoryId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, productId);
            ps.setInt(2, categoryId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean deleteByProductId(int productId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM aux_product_category WHERE productId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, productId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
