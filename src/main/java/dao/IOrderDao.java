package dao;

import entities.Order;
import entities.User;

import java.util.List;

public interface IOrderDao {

    public abstract List<Order> getAllUserOrders(User user);

    public abstract Order getOrderById(int id);

    public abstract Integer insertOrder(Order order);

    public abstract boolean updatedOrder(Order order);

    public abstract boolean deleteOrderById(int id);

}
