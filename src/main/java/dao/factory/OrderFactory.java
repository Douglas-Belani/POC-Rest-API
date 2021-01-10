package dao.factory;

import entities.Address;
import entities.Order;
import entities.OrderStatus;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderFactory {

    public static Order getOrderFromResultSet(ResultSet rs, OrderStatus orderStatus,
                                              Address deliveryAddress, User user) throws SQLException {
        Order order = new Order(rs.getInt("orderId"), rs.getDate("date").toLocalDate(),
                orderStatus, deliveryAddress, user);
        order.setTotalPrice(rs.getDouble("total"));
        return order;
    }

}
