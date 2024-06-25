package com.example.warehouseserver.service;

import com.example.warehouseserver.models.Warehouse;

import java.util.List;

public interface WarehouseServer {
    Warehouse saveWarehouse(Warehouse warehouse);

    void deleteWarehouse(Long warehouseId);

    Warehouse updateWarehouse(Warehouse warehouse);

    Warehouse getWarehouseById(Long warehouseId);

    List<Warehouse> getAllWarehouses();
}
