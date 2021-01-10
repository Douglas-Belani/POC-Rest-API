package dao.impl;

import connection.H2Connection;
import dao.IOrderItemDao;
import entities.OrderItem;
import entities.Product;
import entities.Rate;
import entities.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class OrderItemDaoImplTest {

    private static IOrderItemDao orderItemDao;

    @BeforeClass
    public static  void initialize() {
        orderItemDao = new OrderItemDaoImpl(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_getAllOrderItemsByOrder_Should_Return_OrderItems_Id_4_5_For_Order_Id_3() {
        Product expectedProduct1 = new Product(1, "product1", 50.0,
                "product 1 description",
                20, null, new Rate(1, 2, 0));
        Product expectedProduct2 = new Product(2, "product2", 50.0,
                "product 2 description",
                15, null, new Rate(2, 1, 1));

        OrderItem expectedOrderItem4 = new OrderItem(4, 4, expectedProduct1);
        OrderItem expectedOrderItem5 = new OrderItem(5, 2, expectedProduct2);

        int orderId = 3;

        List<OrderItem> orderItems = orderItemDao.getAllOrderItemsByOrderId(orderId);

        Assert.assertEquals(2, orderItems.size());

        assertThat(expectedOrderItem4, samePropertyValuesAs(orderItems.get(0)));
        assertThat(expectedOrderItem5, samePropertyValuesAs(orderItems.get(1)));

    }

    @Test
    public void test_getAllOrderItemsByOrder_Should_Return_Empty_List_For_Nonexistent_Order_Item_Id() {
        int nonExistentOrderItemId = 10;

        List<OrderItem> orderItems = orderItemDao.getAllOrderItemsByOrderId(nonExistentOrderItemId);

        Assert.assertEquals(0, orderItems.size());
    }

    @Test
    public void test_insertOrderItem_Should_Return_Order_Item_Id_On_Successful_Insert() {
        User expectedUser1 = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));

        Product expectedProduct1 = new Product(1, "product1", 50.0, "product 1 description",
                20, expectedUser1, new Rate(1 ,2, 0));

        OrderItem newOrderItem = new OrderItem(null, 3, expectedProduct1);

        int orderId = 1;

        Integer orderItemId =  orderItemDao.insertOrderItem(newOrderItem, orderId);
        newOrderItem.setOrderItemId(orderItemId);
        Assert.assertTrue(orderItemDao.getAllOrderItemsByOrderId(orderId).contains(newOrderItem));
    }

    @Test
    public void test_deleteById_Should_Return_False_For_Nonexistent_OrderItem_Id() {
        int nonExistentOrderItemId = 20;
        boolean deleted = orderItemDao.deleteOrderItemById(nonExistentOrderItemId);
        Assert.assertFalse(deleted);
    }

}
