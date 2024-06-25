package com.example.productserver.configResponseMessage;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long productId) {
        super("Sản phẩm với ID " + productId + " không tồn tại");
    }
}
