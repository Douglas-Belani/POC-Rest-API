package entities;

public enum OrderStatus {

    Pendent(1, "PENDENT"),
    Paid(2, "PAID"),
    Shipping(3, "SHIPPING"),
    Delivered(4, "DELIVERED"),
    Canceled(5, "CANCELED");

    private final int code;
    private final String description;

    private OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getOrderStatusCode() {
        return this.code;
    }

    public String getOrderStatusDescription() {
        return this.description;
    }

    public static OrderStatus getOrderStatus(int code) {
        OrderStatus status = null;
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (code == orderStatus.getOrderStatusCode()) {
                status = orderStatus;
            }
        }
        return status;
    }
}
