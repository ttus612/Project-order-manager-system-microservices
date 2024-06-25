package com.example.warehouseserver.repository;

import com.example.warehouseserver.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
