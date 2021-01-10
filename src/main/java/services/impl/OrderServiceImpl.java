package services.impl;

import dao.IOrderDao;
import dao.IOrderItemDao;
import dao.IProductDao;
import dao.impl.OrderDaoImpl;
import dao.impl.OrderItemDaoImpl;
import dao.impl.ProductDaoImpl;
import entities.Order;
import entities.OrderItem;
import entities.OrderStatus;
import entities.User;
import resources.exception.ResourceNotFoundException;
import services.IAddressService;
import services.IEmailService;
import services.IOrderService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements IOrderService {

    private Connection conn;
    private IOrderItemDao orderItemDao;
    private IOrderDao orderDao;
    private IProductDao productDao;
    private IEmailService emailService;
    private IAddressService addressService;

    public OrderServiceImpl(Connection conn) {
        this(new OrderItemDaoImpl(conn), new OrderDaoImpl(conn), new AddressServiceImpl(conn), new ProductDaoImpl(conn));
        this.conn = conn;
    }

    public OrderServiceImpl(IOrderItemDao orderItemDao, IOrderDao orderDao, IAddressService addressService,
            IProductDao productDao) {
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
        this.addressService = addressService;
        this.emailService = new EmailServiceImpl();
        this.productDao = productDao;
    }

    public OrderServiceImpl(Connection conn, IOrderItemDao orderItemDao, IOrderDao orderDao,
                            IEmailService emailService, IAddressService addressService, IProductDao productDao) {
        this.conn = conn;
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
        this.emailService = emailService;
        this.addressService = addressService;
        this.productDao = productDao;
    }


    @Override
    public List<Order> getUserOrders(User user) {
        return orderDao.getAllUserOrders(user);
    }

    @Override
    public Order getOrderById(User user, Integer id) {
        Order order = orderDao.getOrderById(id);

        if (order == null) {
            throw new ResourceNotFoundException("Order id " + id + " not found");
        }

        if (!order.getUser().equals(user)) {
            throw new UnauthorizedException("Can't access another user order.");
        }

        List<OrderItem> orderItems = orderItemDao.getAllOrderItemsByOrderId(order.getOrderId());
        orderItems.forEach(orderItem -> order.addOrderItemToOrderItems(orderItem));

        return order;
    }

    @Override
    public Integer insertOrder(Order order) throws SQLException {
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one order item.");
        }

        if (order.getDeliveryAddress() == null) {
            throw new IllegalArgumentException("Order must have a delivery address.");
        }

        try {
            conn.setAutoCommit(false);
            if (order.getDeliveryAddress().getAddressId() == null) {
                Integer addressId = addressService
                        .insertAddress(order.getDeliveryAddress(), order.getUser());

                if (addressId == null) {
                    conn.rollback();
                    return null;
                }

                order.getDeliveryAddress().setAddressId(addressId);
            }

            order.setOrderStatus(OrderStatus.Pendent);
            Integer orderId = orderDao.insertOrder(order);

            if (orderId == null) {
                conn.rollback();
                return null;
            }

            order.setOrderId(orderId);
            order.getOrderItems().forEach(orderItem -> {
                orderItemDao.insertOrderItem(orderItem, order.getOrderId());
                productDao.updateProduct(orderItem.getProduct());});

            conn.commit();

            Thread thread = new Thread(() -> {
                emailService.sendEmail(order.getUser().getEmail(), "New order registered!",
                        order.toString());
            });
            thread.start();
            return orderId;

        } catch (SQLException e) {
            conn.rollback();
            throw e;

        } finally {
            conn.setAutoCommit(true);
        }
    }
}
