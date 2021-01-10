package entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderItemTest {

    @Mock
    private Product product;
    private OrderItem orderItem;

    @Before
    public void initialize() {
        orderItem = new OrderItem(1, 5, product);
    }

    @Test
    public void test_setQuantity_Should_Throw_IllegalArgumentException_For_Negative_Quantity() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                orderItem.setQuantity(0));
        Assert.assertEquals("Invalid order item quantity.", thrown.getMessage());
    }

    @Test
    public void test_setQuantity_Should_Throw_IllegalArgumentException_For_Quantity_Greater_Than_Stock() {
        Mockito.when(product.getStock()).thenReturn(5);
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                orderItem.setQuantity(6));
        Assert.assertEquals("Invalid order item quantity.", thrown.getMessage());
    }

    @Test
    public void test_setQuantity_Should_Successfully_Change_OrderItem_Quantity_For_Quantity_Greater_Than_0_And_Lesser_Than_Product_Stock() {
        Integer quantity = 2;
        Mockito.when(product.getStock()).thenReturn(5);
        Mockito.doNothing().when(product).setStock(Mockito.anyInt());
        orderItem.setQuantity(2);
        Assert.assertEquals(Integer.valueOf(quantity), Integer.valueOf(orderItem.getQuantity()));
    }

   @Test
    public void test_setProduct_Should_Throw_IllegalArgumentException_When_Null_Product_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                orderItem.setProduct(null));
        Assert.assertEquals("Product must not be empty.", thrown.getMessage());
   }

   @Test
    public void test_setProduct_Should_Successfully_Change_Product_For_Non_Null_Product() {
        Product product = new Product(1, "product", 100.0,
                "description", 15, null, null);
        orderItem.setProduct(product);
       Assert.assertEquals(product, orderItem.getProduct());
   }
}
