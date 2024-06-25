package com.example.warehouseserver.service.impl;

import com.example.warehouseserver.configResponseMessage.WarehouseNotFoundException;
import com.example.warehouseserver.models.Warehouse;
import com.example.warehouseserver.repository.WarehouseRepository;
import com.example.warehouseserver.service.WarehouseServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WarehouseServerImpl implements WarehouseServer {
    private WarehouseRepository warehouseRepository;

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String REDIS_KEY = "WAREHOUSE";
    @Override
    public Warehouse saveWarehouse(Warehouse warehouse) {
        System.out.println("Called saveWarehouse() into DB & Redis");
        try {
            warehouseRepository.save(warehouse);
            redisTemplate.opsForHash().put(REDIS_KEY, warehouse.getId().toString(), warehouse);
            return warehouse;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteWarehouse(Long warehouseId) {
        System.out.println("Called deleteWarehouse() from DB & redis: " + warehouseId);
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> new WarehouseNotFoundException(warehouseId));
        warehouseRepository.delete(warehouse);
        redisTemplate.opsForHash().delete(REDIS_KEY, warehouseId.toString());
    }

    @Override
    public Warehouse updateWarehouse(Warehouse warehouse) {
        Warehouse existingWarehouse = warehouseRepository.findById(warehouse.getId()).orElse(null);
        if (existingWarehouse != null) {
            System.out.println("Called updateWarehouse() from DB & redis: " + existingWarehouse);
            existingWarehouse.setName(warehouse.getName());
            existingWarehouse.setLocation(warehouse.getLocation());
            existingWarehouse.setCapacity(warehouse.getCapacity());
            existingWarehouse.setManagerName(warehouse.getManagerName());
            existingWarehouse.setManagerEmail(warehouse.getManagerEmail());
            existingWarehouse.setPhoneNumber(warehouse.getPhoneNumber());

            // Cập nhật bản ghi trong cơ sở dữ liệu MariaDB
            existingWarehouse = warehouseRepository.save(existingWarehouse);

            // Cập nhật bản ghi tương ứng trong Redis
            System.out.println("Called updateWarehouse() into Redis : " + existingWarehouse);
            redisTemplate.delete(REDIS_KEY + "::" + existingWarehouse.getId().toString());
            redisTemplate.opsForHash().put(REDIS_KEY, existingWarehouse.getId().toString(), existingWarehouse);
        }
        return existingWarehouse;
    }

    @Override
    public Warehouse getWarehouseById(Long warehouseId) {
        System.out.println("Called getWarehouseById() from redis: " + warehouseId);
        return (Warehouse) redisTemplate.opsForHash().get(REDIS_KEY, warehouseId.toString());
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouseList;
        warehouseList = redisTemplate.opsForHash().values(REDIS_KEY);
        return warehouseList;
    }
}
