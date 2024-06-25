package com.example.warehouseserver.controller;

import com.example.warehouseserver.configResponseMessage.ResponseMessage;
import com.example.warehouseserver.configResponseMessage.WarehouseNotFoundException;
import com.example.warehouseserver.models.Warehouse;
import com.example.warehouseserver.service.WarehouseServer;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/warehouses")
@AllArgsConstructor
@EnableCaching
@RateLimiter(name = "warehouseService")
public class WarehouseController {
    private WarehouseServer warehouseServer;
    @PostMapping
    public ResponseEntity<Warehouse> saveWarehouse(@RequestBody Warehouse warehouse, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Warehouse savedWarehouse = warehouseServer.saveWarehouse(warehouse);
        if (savedWarehouse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(savedWarehouse, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#warehouseId", value = "WAREHOUSE")
    public ResponseEntity deleteWarehouse(@PathVariable("id") Long warehouseId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        try {
            warehouseServer.deleteWarehouse(warehouseId);
            return ResponseEntity.ok(new ResponseMessage("Xóa kho dùng thành công"));
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Warehouse> updateWarehouse(@RequestBody Warehouse warehouse, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Warehouse updatedWarehouse = warehouseServer.updateWarehouse(warehouse);
        return new ResponseEntity<>(updatedWarehouse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#warehouseId", value = "WAREHOUSE")
    public Warehouse getWarehouseById(@PathVariable("id") Long warehouseId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Warehouse warehouse = warehouseServer.getWarehouseById(warehouseId);
        return  warehouse;
    }

    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses(@RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        List<Warehouse> warehouses = warehouseServer.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }
}
