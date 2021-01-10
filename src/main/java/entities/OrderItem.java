package entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Objects;

public class OrderItem {

    private Integer orderItemId;
    private int quantity;
    private double subTotal;

    private Product product;

    public OrderItem() {

    }

    public OrderItem(Integer orderItemId, int quantity, Product product) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.product = product;
        calculateSubTotal(quantity);
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0 || quantity > product.getStock()) {
            throw new IllegalArgumentException("Invalid order item quantity.");
        }

        product.setStock(product.getStock() - quantity);
        calculateSubTotal(quantity);
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    private void calculateSubTotal(int quantity) {
        subTotal = quantity * product.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be empty.");
        }

        this.product = product;
    }

    @Override
    public String toString() {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            writer.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Double.compare(orderItem.getSubTotal(), getSubTotal()) == 0 &&
                Objects.equals(getOrderItemId(), orderItem.getOrderItemId()) &&
                Objects.equals(getQuantity(), orderItem.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderItemId(), getQuantity(), getSubTotal());
    }
}
