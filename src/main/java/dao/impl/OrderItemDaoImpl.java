package dao.impl;

import dao.IOrderItemDao;
import dao.connection.DatabaseConnection;
import dao.factory.OrderItemFactory;
import dao.factory.ProductFactory;
import dao.factory.RateFactory;
import entities.OrderItem;
import entities.Product;
import entities.Rate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements IOrderItemDao {

    private Connection conn;

    public OrderItemDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<OrderItem> getAllOrderItemsByOrderId(int orderId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrderItem> orderItems = new ArrayList<>();

        try {
            String query = "SELECT `oi`.orderItemId, `oi`.subTotal, `oi`.quantity, " +
                           "`p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                           "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                           "FROM orderItem `oi` " +
                           "INNER JOIN product `p` ON `oi`.productId = `p`.productId " +
                           "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                           "WHERE `oi`.orderId = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);
                    OrderItem orderItem = OrderItemFactory.getOrderItemFromResultSet(rs, product);
                    orderItems.add(orderItem);
                    rs.next();
                }
            }

            return orderItems;

        } catch (SQLException e) {
            e.printStackTrace();
            return orderItems;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertOrderItem(OrderItem orderItem, int orderId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO orderItem VALUES (NULL, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, orderItem.getSubTotal());
            ps.setInt(2, orderItem.getQuantity());
            ps.setInt(3, orderItem.getProduct().getProductId());
            ps.setInt(4, orderId);
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
    public boolean deleteOrderItemById(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM orderItem WHERE orderItemId = ?";
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

    @Override
    public boolean deleteOrderItemByOrderId(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM orderItem WHERE orderItem.orderId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
