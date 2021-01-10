package services.impl;

import dao.IOrderDao;
import dao.IOrderItemDao;
import dao.IProductDao;
import entities.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import resources.exception.ResourceNotFoundException;
import services.IAddressService;
import services.IEmailService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OrderServiceTest {

    @Mock
    private Connection conn;

    @Mock
    private IOrderItemDao orderItemDao;

    @Mock
    private IOrderDao orderDao;

    @Mock
    private IEmailService emailService;

    @Mock
    private IAddressService addressService;

    @Mock
    private IProductDao productDao;

    private OrderServiceImpl orderService;

    @Before
    public void initialize() {
        this.orderService = new OrderServiceImpl(conn, orderItemDao, orderDao, emailService,
                addressService, productDao);
    }

    @Test
    public void test_getOrderById_Should_Throw_UnauthorizedException_If_User_Id_Is_Invalid_For_Given_Order_Id() {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22",
                "email2@gmail.com", "p@ssword2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city2", state);
        Address address = new Address(1, "neighborhood1", "1",
                "11111-111", "street1", "complement1", city);
        Order order = new Order(1, LocalDate.now(), OrderStatus.Paid, address, user);

        Mockito.when(orderDao.getOrderById(order.getOrderId())).thenReturn(order);
        UnauthorizedException thrown = Assert.assertThrows(UnauthorizedException.class, () ->
                orderService.getOrderById(user2, order.getOrderId()));
        Assert.assertEquals("Can't access another user order.", thrown.getMessage());
    }

    @Test
    public void test_getOrderById_Should_Throw_ResourceNotFoundException_If_Nonexistent_Order_Id_Is_Passed() {
        int nonExistentOrderId = 8764;
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22",
                "email2@gmail.com", "p@ssword2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city2", state);
        Address address = new Address(1, "neighborhood1", "1",
                "11111-111", "street1", "complement1", city);
        Order order = new Order(1, LocalDate.now(), OrderStatus.Paid, address, user);

        ResourceNotFoundException thrown = Assert.assertThrows(ResourceNotFoundException.class, () ->
                orderService.getOrderById(user, nonExistentOrderId));
        Assert.assertEquals("Order id " + nonExistentOrderId + " not found", thrown.getMessage());
    }

    @Test
    public void test_getOrderById_Should_Return_Order_And_Its_OrderItems_For_Valid_User_And_Order_Id() {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22",
                "email2@gmail.com", "p@ssword2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city2", state);
        Address address = new Address(1, "neighborhood1", "1",
                "11111-111", "street1", "complement1", city);
        Order order = new Order(1, LocalDate.now(), OrderStatus.Paid, address, user);

        Product product = new Product(1, "product1", 100.00,
                "description1", 12, user2,
                new Rate(1, 2, 0));
        Product product2 = new Product(2, "product2", 250.00,
                "description2", 15, user2,
                new Rate(2, 1, 0));

        OrderItem orderItem1 = new OrderItem(1, 2, product);
        OrderItem orderItem2 = new OrderItem(2, 5, product2);

        Mockito.when(orderDao.getOrderById(order.getOrderId())).thenReturn(order);
        Mockito.when(orderItemDao.getAllOrderItemsByOrderId(order.getOrderId()))
                .thenReturn(Arrays.asList(orderItem1, orderItem2));

        Order orderReturned = orderService.getOrderById(user, order.getOrderId());
        assertThat(order, samePropertyValuesAs(orderReturned, "orderItems"));
        Assert.assertEquals(2, orderReturned.getOrderItems().size());
        assertThat(orderItem1, samePropertyValuesAs(orderReturned.getOrderItems().get(0)));
        assertThat(orderItem2, samePropertyValuesAs(orderReturned.getOrderItems().get(1)));

    }

    @Test
    public void test_insertOrder_Should_Throw_IllegalArgumentException_If_Order_OrderItems_Is_Empty() {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city2", state);
        Address address = new Address(1, "neighborhood1", "1",
                "11111-111", "street1", "complement1", city);
        Order order = new Order(1, LocalDate.now(), OrderStatus.Paid, address, user);

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class,
                () -> orderService.insertOrder(order));
        Assert.assertEquals("Order must have at least one order item.", thrown.getMessage());
    }

    @Test
    public void test_insertOrder_Should_Throw_IllegalArgumentException_If_DeliveryAddress_Is_Null() {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22",
                "email2@gmail.com", "p@ssword2", LocalDate.now());
        Order order = new Order(1, LocalDate.now(), OrderStatus.Paid, null, user);

        Product product = new Product(1, "product1", 100.00,
                "description1", 12, user2,
                new Rate(1, 2, 0));
        Product product2 = new Product(2, "product2", 250.00,
                "description2", 15, user2,
                new Rate(2, 1, 0));

        OrderItem orderItem1 = new OrderItem(1, 2, product);
        OrderItem orderItem2 = new OrderItem(2, 5, product2);
        order.addOrderItemToOrderItems(orderItem1);
        order.addOrderItemToOrderItems(orderItem2);

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class,
                () -> orderService.insertOrder(order));
        Assert.assertEquals("Order must have a delivery address.", thrown.getMessage());
    }

    @Test
    public void test_insertOrder_Should_Successfully_Insert_New_Order_If_Address_Id_Is_Null() throws SQLException {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22",
                "email2@gmail.com", "p@ssword2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city2", state);
        Address address = new Address(null, "neighborhood1", "1",
                "11111-111", "street1", "complement1", city);
        Order order = new Order(0, LocalDate.now(), null, address, user);

        Product product = new Product(1, "product1", 100.00,
                "description1", 12, user2,
                new Rate(1, 2, 0));
        Product product2 = new Product(2, "product2", 250.00,
                "description2", 15, user2,
                new Rate(2, 1, 0));

        OrderItem orderItem1 = new OrderItem(1, 2, product);
        OrderItem orderItem2 = new OrderItem(2, 5, product2);
        order.addOrderItemToOrderItems(orderItem1);
        order.addOrderItemToOrderItems(orderItem2);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressService.insertAddress(address, user)).thenReturn(1);
        Mockito.when(orderDao.insertOrder(order)).thenReturn(1);
        Mockito.when(orderItemDao.insertOrderItem(orderItem1, order.getOrderId())).thenReturn(1);
        Mockito.when(orderItemDao.insertOrderItem(orderItem2, order.getOrderId())).thenReturn(2);
        Mockito.doNothing().when(conn).commit();
        orderService.insertOrder(order);
        Assert.assertEquals(Integer.valueOf(1), order.getOrderId());
        Assert.assertEquals(Integer.valueOf(1), address.getAddressId());
        Mockito.verify(conn, Mockito.times(1)).commit();
    }

    @Test
    public void test_insertOrder_Should_Return_Null_If_Insert_Address_Returns_Null() throws SQLException {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22",
                "email2@gmail.com", "p@ssword2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city2", state);
        Address address = new Address(null, "neighborhood1", "1",
                "11111-111", "street1", "complement1", city);
        Order order = new Order(0, LocalDate.now(), OrderStatus.Paid, address, user);

        Product product = new Product(1, "product1", 100.00,
                "description1", 12, user2,
                new Rate(1, 2, 0));
        Product product2 = new Product(2, "product2", 250.00,
                "description2", 15, user2,
                new Rate(2, 1, 0));

        OrderItem orderItem1 = new OrderItem(1, 2, product);
        OrderItem orderItem2 = new OrderItem(2, 5, product2);
        order.addOrderItemToOrderItems(orderItem1);
        order.addOrderItemToOrderItems(orderItem2);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressService.insertAddress(address, user)).thenReturn(null);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertNull(orderService.insertOrder(order));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_insertOrder_Should_Return_Null_If_OrderDao_Insert_Returns_Null() throws SQLException {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "passW0rd@", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22",
                "email2@gmail.com", "p@ssword2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city2", state);
        Address address = new Address(null, "neighborhood1", "1",
                "11111-111", "street1", "complement1", city);
        Order order = new Order(0, LocalDate.now(), null, address, user);

        Product product = new Product(1, "product1", 100.00,
                "description1", 12, user2,
                new Rate(1, 2, 0));
        Product product2 = new Product(2, "product2", 250.00,
                "description2", 15, user2,
                new Rate(2, 1, 0));

        OrderItem orderItem1 = new OrderItem(1, 2, product);
        OrderItem orderItem2 = new OrderItem(2, 5, product2);
        order.addOrderItemToOrderItems(orderItem1);
        order.addOrderItemToOrderItems(orderItem2);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressService.insertAddress(address, user)).thenReturn(1);
        Mockito.when(orderDao.insertOrder(order)).thenReturn(null);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertNull(orderService.insertOrder(order));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

}
