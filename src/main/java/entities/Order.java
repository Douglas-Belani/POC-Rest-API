package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private Integer orderId;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    private Double totalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus orderStatus;
    private Address deliveryAddress;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {

    }

    public Order(Integer orderId, LocalDate date, OrderStatus orderStatus,
                 Address deliveryAddress, User user) {
        this.orderId = orderId;
        this.date = date;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
        this.user = user;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null.");
        }

        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private void calculateTotalPrice(OrderItem orderItem) {
        if (totalPrice == null) {
            totalPrice = orderItem.getSubTotal();

        } else {
        totalPrice += orderItem.getSubTotal();
        }
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == null) {
            throw new IllegalArgumentException("Order status must not be null.");
        }

        if (this.orderStatus != null && orderStatus.getOrderStatusCode() < this.orderStatus.getOrderStatusCode()) {
            throw new IllegalArgumentException("Invalid order status.");
        }

        this.orderStatus = orderStatus;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        if (deliveryAddress == null) {
            throw new IllegalArgumentException("Address must not be null.");
        }

        this.deliveryAddress = deliveryAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }

        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItemToOrderItems(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("Order item must not be null.");
        }

        calculateTotalPrice(orderItem);
        orderItems.add(orderItem);
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
        for (OrderItem orderItem : orderItems) {
            if (totalPrice == null) {
                totalPrice = orderItem.getSubTotal();
            } else {
                totalPrice += orderItem.getSubTotal();
            }
        }
    }

    public LocalDate calculatePaymentExpirationDate() {
        if (date.plusDays(5).getDayOfWeek() == DayOfWeek.SATURDAY) {
            return date.plusDays(7);

        } else if (date.plusDays(5).getDayOfWeek() == DayOfWeek.SUNDAY) {
            return date.plusDays(6);

        } else {
            return date.plusDays(5);
        }
    }

    @Override
    public String toString() {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            return writer.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getOrderId(), order.getOrderId()) &&
                Objects.equals(getDate(), order.getDate()) &&
                getOrderStatus() == order.getOrderStatus() &&
                Objects.equals(getDeliveryAddress(), order.getDeliveryAddress()) &&
                Objects.equals(getUser(), order.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getDate(), getOrderStatus(), getDeliveryAddress(), getUser());
    }
}
