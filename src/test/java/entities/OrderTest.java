package entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class OrderTest {

    @Mock
    private OrderItem orderItem;

    private Order order;

    @Before
    public void initialize() {
        this.order = new Order(1, LocalDate.now(), OrderStatus.Pendent,
                null, null);
    }

    @Test
    public void test_setDate_Should_Throw_IllegalArgumentException_When_Null_Date_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                order.setDate(null));
        Assert.assertEquals("Date must not be null.", thrown.getMessage());
    }

    @Test
    public void test_setDate_Should_Successfully_Change_Date_For_Non_Null_Date() {
        LocalDate newDate = LocalDate.now().plusDays(1);
        order.setDate(newDate);
        Assert.assertEquals(newDate, order.getDate());
    }

    @Test
    public void test_setOrderStatus_Should_Throw_IllegalArgumentException_For_Null_OrderStatus() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                order.setOrderStatus(null));
        Assert.assertEquals("Order status must not be null.", thrown.getMessage());

    }

    @Test
    public void test_setOrderStatus_Should_Successfully_Change_OrderStatus_For_Non_Null_OrderStatus() {
        OrderStatus orderStatus = OrderStatus.Paid;
        order.setOrderStatus(orderStatus);
        assertThat(orderStatus, samePropertyValuesAs(order.getOrderStatus()));
    }

    @Test
    public void test_setDeliveryAddress_Should_Throw_IllegalArgumentException_For_Non_Null_DeliveryAddress() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                order.setDeliveryAddress(null));
        Assert.assertEquals("Address must not be null.", thrown.getMessage());
    }

    @Test
    public void test_setDeliveryAddress_Should_Successfully_Change_DeliveryAddress_For_Non_Null_DeliveryAddress() {
        State state = new State(1, "SP");
        City city = new City(1, "city1", state);
        Address deliveryAddress = new Address(1, "neighborhood", "1",
                "111111-111", "street1", null, city);
        order.setDeliveryAddress(deliveryAddress);
        assertThat(deliveryAddress, samePropertyValuesAs(order.getDeliveryAddress()));
    }

    @Test
    public void test_setUser_Should_Throw_IllegalArgumentException_When_Null_User_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                order.setUser(null));
        Assert.assertEquals("User must not be null.", thrown.getMessage());
    }

    @Test
    public void test_setUser_Should_Successfully_Change_User_For_Non_Null_User() {
        User user = new User(1, "fullName", "111111111-111",
                "email@gmail.com", "password", LocalDate.now());
        order.setUser(user);
        Assert.assertEquals(user, order.getUser());
    }

    @Test
    public void test_addOrderItem_Should_Throw_IllegalArgumentException_For_Null_OrderItem() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                order.addOrderItemToOrderItems(null));
        Assert.assertEquals("Order item must not be null.", thrown.getMessage());
    }

    @Test
    public void test_addOrderItem_Should_Successfully_Add_OrderItem_To_orderItems_When_Non_Null_OrderItem_Is_Passed() {
        Mockito.when(orderItem.getSubTotal()).thenReturn(50.0);
        Assert.assertEquals(0, order.getOrderItems().size());
        order.addOrderItemToOrderItems(orderItem);
        Assert.assertEquals(1, order.getOrderItems().size());
        assertThat(orderItem, samePropertyValuesAs(order.getOrderItems().get(0)));
    }

    @Test
    public void test_calculateExpirationDate_Should_Return_Date_Plus_6_If_Date_Plus_5_Is_Sunday() {
        order.setDate(LocalDate.of(2020, 11, 24));
        Assert.assertEquals(LocalDate.of(2020, 11, 30),
                order.calculatePaymentExpirationDate());

    }

    @Test
    public void test_calculateExpirationDate_Should_Return_Date_Plus_7_If_Date_Plus_5_Is_Saturday() {
        order.setDate(LocalDate.of(2020, 11, 23));
        Assert.assertEquals(LocalDate.of(2020, 11, 30),
                order.calculatePaymentExpirationDate());
    }

    @Test
    public void test_calculateExpirationDate_Should_Return_Date_Plus_5_If_Date_Plus_5_Is_Not_Saturday_Or_Sunday() {
        order.setDate(LocalDate.of(2020, 11, 22));
        Assert.assertEquals(LocalDate.of(2020, 11, 27),
                order.calculatePaymentExpirationDate());
    }
}
