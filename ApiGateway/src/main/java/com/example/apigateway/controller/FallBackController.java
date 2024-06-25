package com.example.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallBackController {
    @GetMapping("/orderServerFallback")
    public ResponseEntity<String> fallbackOrderServer() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("[CircuitBreaker - ApiGateway] Service Order is not available in Discovery Server!");
    }

    @GetMapping("/userServerFallback")
    public ResponseEntity<String> fallbackUserServer() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("[CircuitBreaker - ApiGateway] Service User is not available in Discovery Server!");
    }

    @GetMapping("/productServerFallback")
    public ResponseEntity<String> fallbackProductServer() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("[CircuitBreaker - ApiGateway] Service Product is not available in Discovery Server!");
    }

    @GetMapping("/shippingServerFallback")
    public ResponseEntity<String> fallbackShippingServer() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("[CircuitBreaker - ApiGateway] Service Shipping is not available in Discovery Server!");
    }

    @GetMapping("/supplierServerFallback")
    public ResponseEntity<String> fallbackSupplierServer() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("[CircuitBreaker - ApiGateway] Service Supplier is not available in Discovery Server!");
    }

    @GetMapping("/warehouseServerFallback")
    public ResponseEntity<String> fallbackWarehouseServer() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("[CircuitBreaker - ApiGateway] Service Warehouse is not available in Discovery Server!");
    }
    @GetMapping("/requestWarehouseServerFallback")
    public ResponseEntity<String> fallbackRequestWarehouseServer() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("[CircuitBreaker - ApiGateway] Service RequestWarehouse is not available in Discovery Server!");
    }
}
