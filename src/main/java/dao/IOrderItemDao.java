package dao;

import entities.Order;
import entities.OrderItem;

import java.util.List;

public interface IOrderItemDao {

    public abstract List<OrderItem> getAllOrderItemsByOrderId(int orderId);

    public abstract Integer insertOrderItem(OrderItem orderItem, int orderId);

    public abstract boolean deleteOrderItemById(int id);

    public abstract boolean deleteOrderItemByOrderId(int id);

}
