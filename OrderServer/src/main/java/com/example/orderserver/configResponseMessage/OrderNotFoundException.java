package com.example.orderserver.configResponseMessage;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long orderId) {
        super("Order với ID " + orderId + " không tồn tại");
    }
}
