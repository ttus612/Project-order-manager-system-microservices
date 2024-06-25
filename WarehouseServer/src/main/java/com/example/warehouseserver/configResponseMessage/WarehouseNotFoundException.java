package com.example.warehouseserver.configResponseMessage;

public class WarehouseNotFoundException extends RuntimeException{
    public WarehouseNotFoundException(Long warehouseId) {
        super("Kho với dùng với ID " + warehouseId + " không tồn tại");
    }
}
