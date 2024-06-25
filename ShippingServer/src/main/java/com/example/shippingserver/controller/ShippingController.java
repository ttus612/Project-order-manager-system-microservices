package com.example.shippingserver.controller;

import com.example.shippingserver.configResponseMessage.ResponseMessage;
import com.example.shippingserver.configResponseMessage.ShippingNotFoundException;
import com.example.shippingserver.models.Shipping;
import com.example.shippingserver.service.ShippingServer;
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
@RequestMapping("api/shippings")
@AllArgsConstructor
@EnableCaching
@RateLimiter(name= "shippingService")
public class ShippingController {
    private ShippingServer shippingServer;

    @PostMapping
    public ResponseEntity<Shipping> saveShipping(@RequestBody Shipping shipping, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Shipping saveShipping = shippingServer.saveShipping(shipping);
        if (saveShipping == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(saveShipping, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#shippingId", value = "SHIPPING")
    public ResponseEntity deleteShipping(@PathVariable("id") Long shippingId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        try {
            shippingServer.deleteShipping(shippingId);
            return ResponseEntity.ok(new ResponseMessage("Xóa đơn vị vẫn chuyển thành công"));
        } catch (ShippingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Shipping> updateShipping(@RequestBody Shipping shipping, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Shipping updatedShipping = shippingServer.updateShipping(shipping);
        return new ResponseEntity<>(updatedShipping, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#shippingId", value = "SHIPPING")
    public Shipping getShippingById(@PathVariable("id") Long shippingId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Shipping shipping = shippingServer.getShippingById(shippingId);
        return  shipping;
    }

    @GetMapping
    public ResponseEntity<List<Shipping>> getAllShippings(@RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        List<Shipping> shippings = shippingServer.getAllShipping();
        return ResponseEntity.ok(shippings);
    }
}
