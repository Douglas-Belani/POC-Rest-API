package services;

import entities.Order;
import entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IOrderService {

    public abstract List<Order> getUserOrders(User user);

    public abstract Order getOrderById(User user, Integer id);

    public abstract Integer insertOrder(Order order) throws SQLException;

}
