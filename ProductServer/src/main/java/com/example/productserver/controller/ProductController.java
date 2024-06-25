package com.example.productserver.controller;

import com.example.productserver.configResponseMessage.ProductNotFoundException;
import com.example.productserver.configResponseMessage.ResponseMessage;
import com.example.productserver.models.Product;
import com.example.productserver.service.ProductService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
@EnableCaching
@RateLimiter(name= "productService")
public class ProductController {
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> saveUser(@RequestBody Product product, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Product savedProduct = productService.saveProduct(product);
        if (savedProduct == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#productId", value = "PRODUCT")
    public ResponseEntity deleteProduct(@PathVariable("id") Long productId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(new ResponseMessage("Xóa người dùng thành công"));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Product updatedProduct = productService.updateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#productId", value = "PRODUCT")
    public Product getUserById(@PathVariable("id") Long productId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Product product = productService.getProductById(productId);
        return  product;
    }

    @GetMapping
    public ResponseEntity<Iterable<Product>> getAllUsers( @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Iterable<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
