package com.example.supplierserver.service;

import com.example.supplierserver.models.Supplier;

import java.util.List;

public interface SupplierService{

    Supplier saveSupplier(Supplier supplier);

    void deleteSupplier(Long supplierId);

    Supplier updateSupplier(Supplier supplier);

    Supplier getSupplierById(Long supplierId);

    List<Supplier> getAllSuppliers();
}
