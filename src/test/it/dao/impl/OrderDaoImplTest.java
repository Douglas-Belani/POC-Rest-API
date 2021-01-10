package dao.impl;

import connection.H2Connection;
import dao.IOrderDao;
import dao.IOrderItemDao;
import entities.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class OrderDaoImplTest {

    private static IOrderDao orderDao;
    private static IOrderItemDao orderItemDao;
    private static Connection conn;

    @BeforeClass
    public static void initialize() {
        conn = H2Connection.getH2DatabaseConnection();
        orderDao = new OrderDaoImpl(conn);
        orderItemDao = new OrderItemDaoImpl(conn);
    }

    @Test
    public void test_getAllOrdersByUser_Should_Return_Orders_Id_1_2_For_User_Id_3() {
        User expectedUser3 = new User(3, "user3", "333333333-33",
                "user3@gmail.com", "user3Password",
                LocalDate.of(2004, 7, 23));

        State expectedState3 = new State(3, "RS");

        City expectedCity4 = new City(4, "city4", expectedState3);

        Address expectedAddress5 = new Address(5, "neighborhood5",
                "5", "555555-555", "street5",
                "complement5", expectedCity4);

        Order expectedOrder1 = new Order(1, LocalDate.of(2019, 8, 15), OrderStatus.getOrderStatus(4),
                expectedAddress5, expectedUser3);
        expectedOrder1.setTotalPrice(90.00);
        Order expectedOrder2 = new Order(2, LocalDate.of(2019, 10, 28), OrderStatus.getOrderStatus(4),
                expectedAddress5, expectedUser3);
        expectedOrder2.setTotalPrice(50.00);

        List<Order> orders = orderDao.getAllUserOrders(expectedUser3);

        Assert.assertEquals(2, orders.size());

        assertThat(expectedOrder1, samePropertyValuesAs(orders.get(0)));
        assertThat(expectedOrder2, samePropertyValuesAs(orders.get(1)));
    }

    @Test
    public void test_getAllOrderByUser_Should_Return_Empty_Order_List_For_User_Id_4() {
        User user4 = new User(4, "user4", "444444444-44",
                "user4@gmail.com", "user4Password",
                LocalDate.of(2003, 4, 3));
        List<Order> orders = orderDao.getAllUserOrders(user4);

        Assert.assertTrue(orders.isEmpty());

    }

    @Test
    public void test_getOrderById_Should_Return_Order_Id_1() {
                User expectedUser3 = new User(3, "user3", "333333333-33",
                        "user3@gmail.com", "user3Password",
                        LocalDate.of(2004, 7, 23));


        State expectedState3 = new State(3, "RS");
        City expectedCity4 = new City(4, "city4", expectedState3);
        Address expectedAddress5 = new Address(5, "neighborhood5",
                "5", "555555-555", "street5",
                "complement5", expectedCity4);

        Order expectedOrder1 = new Order(1, LocalDate.of(2019, 8,
                15), OrderStatus.getOrderStatus(4),
                expectedAddress5, expectedUser3);
        expectedOrder1.setTotalPrice(90.00);

        Order order = orderDao.getOrderById(1);

        assertThat(expectedOrder1, samePropertyValuesAs(order));
    }

    @Test
    public void test_getOrderById_Should_Return_Null_For_Nonexistent_Order_Id() {
        int non_existentOrderId = 10;
        Order order = orderDao.getOrderById(non_existentOrderId);

        Assert.assertNull(order);
    }

    @Test
    public void test_insertOrder_Should_Return_True_For_Successful_Insert() {
        User user = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));
        User user2 = new User(2, "user2", "222222222-22",
                "user2@gmail.com", "user2Password",
                LocalDate.of(2002, 10, 10));
        State state = new State(1, "SP");
        City city = new City(1, "city1", state);

        Address address = new Address(1, "neighborhood1", "1",
                "111111-111", "street1",
                "complement1", city);

        Order newOrder = new Order(null, LocalDate.of(2020, 10, 1),
                OrderStatus.Pendent, address, user);

        Product product3 = new Product(3, "product3", 20.0,
                "product 3 description", 5, user2,
                new Rate(3, 0, 1));

        Category category2 = new Category(2, "category2");
        Category category4 = new Category(4, "category4");

        product3.getCategories().addAll(Arrays.asList(category2, category4));

        OrderItem orderItem1 = new OrderItem(null, 2, product3);

        newOrder.addOrderItemToOrderItems(orderItem1);
        Integer orderId = orderDao.insertOrder(newOrder);
        newOrder.setOrderId(orderId);

        Integer orderItemId = orderItemDao.insertOrderItem(orderItem1, newOrder.getOrderId());
        orderItem1.setOrderItemId(orderItemId);

        Order order = orderDao.getOrderById(orderId);
        assertThat(newOrder, samePropertyValuesAs(order, "orderItems"));

    }

    @Test
    public void test_deleteOrderById_Should_Return_True_For_Successful_Delete() {
        Order order = new Order(null, LocalDate.of(2020, 10, 1)
                , OrderStatus.Pendent,
                new Address(1, null, null, null,
                        null, null, null),
                new User(1, null, null,null, null,
                        null));
        order.setTotalPrice(50.0);

        Integer orderId = orderDao.insertOrder(order);

        boolean deleted = orderDao.deleteOrderById(orderId);

        Assert.assertTrue(deleted);
    }

    @Test
    public void test_deleteOrderById_Should_Return_False_For_Nonexistent_Order_Id() {
        int nonExistentOrderId = 10;
        boolean deleted = orderDao.deleteOrderById(nonExistentOrderId);

        Assert.assertFalse(deleted);
    }

}
