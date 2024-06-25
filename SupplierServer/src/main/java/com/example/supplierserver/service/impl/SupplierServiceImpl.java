package com.example.supplierserver.service.impl;

import com.example.supplierserver.configResponseMessage.SupplierNotFoundException;
import com.example.supplierserver.models.Supplier;
import com.example.supplierserver.repository.SupplierRepository;
import com.example.supplierserver.service.SupplierService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private SupplierRepository supplierRepository;

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String REDIS_KEY = "SUPPLIER";
    @Override
    public Supplier saveSupplier(Supplier supplier) {
        System.out.println("Called saveSupplier() into DB & redis");
        try {
            supplierRepository.save(supplier);
            redisTemplate.opsForHash().put(REDIS_KEY, supplier.getId().toString(), supplier);
            return supplier;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        System.out.println("Called deleteSupplier() from DB & redis: " + supplierId);
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new SupplierNotFoundException(supplierId));
        supplierRepository.delete(supplier);
        redisTemplate.opsForHash().delete(REDIS_KEY, supplierId.toString());
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) {
        Supplier existingSupplier = supplierRepository.findById(supplier.getId()).orElse(null);
        if (existingSupplier != null) {
            System.out.println("Called updateSupplier() from DB & redis: " + existingSupplier);
            existingSupplier.setName(supplier.getName());
            existingSupplier.setAddress(supplier.getAddress());
            existingSupplier.setPhoneNumberContact(supplier.getPhoneNumberContact());
            existingSupplier.setEmailContact(supplier.getEmailContact());
            existingSupplier.setTaxCode(supplier.getTaxCode());

            existingSupplier = supplierRepository.save(existingSupplier);

            System.out.println("Called updateSupplier() into Redis : " + existingSupplier);
            redisTemplate.delete(REDIS_KEY + "::" + existingSupplier.getId().toString());
            redisTemplate.opsForHash().put(REDIS_KEY, existingSupplier.getId().toString(), existingSupplier);
        }
        return existingSupplier;
    }

    @Override
    public Supplier getSupplierById(Long supplierId) {
        System.out.println("Called getSupplierById() from redis: " + supplierId);
        return (Supplier) redisTemplate.opsForHash().get(REDIS_KEY, supplierId.toString());
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers;
        suppliers = redisTemplate.opsForHash().values(REDIS_KEY);
        return suppliers;
    }
}
