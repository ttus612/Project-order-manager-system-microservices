package com.example.supplierserver.configResponseMessage;

public class SupplierNotFoundException extends RuntimeException{
    public SupplierNotFoundException(Long supplierId) {
        super("Nhà cung cấp với ID " + supplierId + " không tồn tại");
    }
}
