package dao.factory;

import entities.OrderItem;
import entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemFactory {

    public static OrderItem getOrderItemFromResultSet(ResultSet rs, Product product) throws SQLException {
        return new OrderItem(rs.getInt("orderItemId"),
                rs.getInt("quantity"), product);
    }

}
