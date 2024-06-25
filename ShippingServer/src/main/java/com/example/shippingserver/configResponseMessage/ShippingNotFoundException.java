package com.example.shippingserver.configResponseMessage;

public class ShippingNotFoundException extends RuntimeException{
    public ShippingNotFoundException(Long shippingId) {
        super("Đơn vị vận chuyển với ID " + shippingId + " không tồn tại");
    }
}
