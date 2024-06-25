package com.example.orderserver.controller;

import com.example.orderserver.dto.ResponseDto;
import com.example.orderserver.service.OrderService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
@EnableCaching
public class OrderController_RateLimiterClient {
    private OrderService orderService;
    private static int count = 1;
    private static boolean addMessage = false; // Biến đánh dấu để xác định khi nào cần thêm message vào ResponseDto

    @GetMapping("/order-RateClient/{id}")
    @RateLimiter(name = "orderService", fallbackMethod = "fallbackRateLimiterServerOrder")
    public ResponseDto getOrderRateLimiter(@PathVariable("id") Long orderId, @RequestHeader("loggedInUser") String userName) {
        addMessage = true;
        ResponseDto responseDto = orderService.getOrder(orderId, userName);
        responseDto.setMessage("Thành công !");
        return responseDto;
    }

    public ResponseDto fallbackRateLimiterServerOrder(Long orderId, String userName, Exception e){
        System.out.println("=================================================RATE LIMITER - CLIENT===============================================");
        System.out.println("Fallback occurred. Error: " + e.getMessage());
        System.out.println("Order ID: " + orderId);
        System.out.println("User Name: " + userName);
        System.out.println("===============================================================================================================");
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("[RateLimiterClient - OrderServer] Too many requests sent to the Product and User servers!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto).getBody();
    }

}
