package dao.impl;

import dao.IOrderDao;
import dao.connection.DatabaseConnection;
import dao.factory.*;
import entities.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements IOrderDao {

    private Connection conn;

    public OrderDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Order> getAllUserOrders(User user) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();

        try {
            String query = "SELECT `o`.orderId, `o`.total,`o`.date, " +
                           "`os`.orderStatusId, `os`.status, " +
                           "`a`.addressId, `a`.neighborhood, `a`.number, `a`.zipCode, `a`.number, " +
                           "`a`.street, `a`.complement, " +
                           "`ci`.cityId, `ci`.cityName, " +
                           "`s`.stateId, `s`.initials " +
                           "FROM `order` `o` " +
                           "INNER JOIN orderStatus `os` ON `o`.orderStatusId = `os`.orderStatusId " +
                           "INNER JOIN address `a` ON `o`.addressId = `a`.addressId " +
                           "INNER JOIN city `ci` ON `a`.cityId = `ci`.cityId " +
                           "INNER JOIN state `s` ON `ci`.stateId = `s`.stateId " +
                           "WHERE `o`.userId = ? " +
                           "ORDER BY `a`.addressId ";
            ps = conn.prepareStatement(query);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    State state = StateFactory.getStateFromResultSet(rs);
                    City city = CityFactory.getCityFromResultSet(rs, state);
                    Address deliveryAddress = AddressFactory.getAddressFromResultSet(rs, city);

                    while (!rs.isAfterLast() &&
                            rs.getInt("addressId") == deliveryAddress.getAddressId()) {

                        Order order = OrderFactory.getOrderFromResultSet(rs,
                                OrderStatus.getOrderStatus(rs.getInt("orderStatusId")),
                                deliveryAddress, user);
                        orders.add(order);
                        rs.next();
                    }
                }
            }

            return orders;

        } catch (SQLException e) {
            e.printStackTrace();
            return orders;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Order getOrderById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Order order = null;

        try {
            String query = "SELECT `o`.orderId, `o`.total,`o`.date, " +
                           "`os`.orderStatusId, `os`.status, " +
                           "`u`.userId, `u`.userName, `u`.cpf, `u`.email, `u`.password, `u`.birthDate, " +
                           "`a`.addressId, `a`.neighborhood, `a`.number, `a`.zipCode, `a`.number, " +
                           "`a`.street, `a`.complement, " +
                           "`ci`.cityId, `ci`.cityName, " +
                           "`s`.stateId, `s`.initials " +
                           "FROM `order` `o` " +
                           "INNER JOIN orderStatus `os` ON `o`.orderStatusId = `os`.orderStatusId " +
                           "INNER JOIN user `u` ON `o`.userId = `u`.userId " +
                           "INNER JOIN address `a` ON `o`.addressId = `a`.addressId " +
                           "INNER JOIN city `ci` ON `a`.cityId = `ci`.cityId " +
                           "INNER JOIN state `s` ON `ci`.stateId = `s`.stateId " +
                           "WHERE `o`.orderId = ? ";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                User user = UserFactory.getUserFromResultSet(rs);
                State state = StateFactory.getStateFromResultSet(rs);
                City city = CityFactory.getCityFromResultSet(rs, state);
                Address address = AddressFactory.getAddressFromResultSet(rs, city);
                order = OrderFactory.getOrderFromResultSet(rs,
                        OrderStatus.getOrderStatus(rs.getInt("orderStatusId")),
                        address, user);
            }

            return order;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertOrder(Order order) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO `order` VALUES (NULL, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(order.getDate()));
            ps.setDouble(2, order.getTotalPrice());
            ps.setInt(3, order.getUser().getUserId());
            ps.setInt(4, order.getOrderStatus().getOrderStatusCode());
            ps.setInt(5, order.getDeliveryAddress().getAddressId());
            ps.execute();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
            return id;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean updatedOrder(Order order) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE `order` " +
                                "SET orderStatus = ? " +
                                "WHERE `order`.orderId = ? ";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, order.getOrderStatus().getOrderStatusCode());

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
    public boolean deleteOrderById(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM `order` WHERE `order`.orderId = ?";
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
