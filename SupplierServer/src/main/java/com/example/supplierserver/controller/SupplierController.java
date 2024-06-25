package com.example.supplierserver.controller;

import com.example.supplierserver.configResponseMessage.ResponseMessage;
import com.example.supplierserver.configResponseMessage.SupplierNotFoundException;
import com.example.supplierserver.models.Supplier;
import com.example.supplierserver.service.SupplierService;
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
@RequestMapping("api/suppliers")
@AllArgsConstructor
@EnableCaching
@RateLimiter(name = "supplierService")
public class SupplierController {
    private SupplierService supplierService;

    @PostMapping
    public ResponseEntity<Supplier> saveSupplier(@RequestBody Supplier supplier, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Supplier saveSupplier = supplierService.saveSupplier(supplier);
        if(saveSupplier == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(saveSupplier, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#supplierId", value = "SUPPLIER")
    public ResponseEntity deleteSupplier(@PathVariable("id") Long supplierId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        try {
            supplierService.deleteSupplier(supplierId);
            return ResponseEntity.ok(new ResponseMessage("Xóa nhà cung cấp thành công"));
        } catch (SupplierNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Supplier> updateSupplier(@RequestBody Supplier supplier, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Supplier updatedSupplier = supplierService.updateSupplier(supplier);
        return new ResponseEntity<>(updatedSupplier, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#supplierId", value = "SUPPLIER")
    public Supplier getSupplierById(@PathVariable("id") Long supplierId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Supplier supplier = supplierService.getSupplierById(supplierId);
        return  supplier;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers(@RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
}
