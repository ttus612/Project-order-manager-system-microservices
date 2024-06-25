package com.example.orderserver.controller;

import com.example.orderserver.configResponseMessage.OrderNotFoundException;
import com.example.orderserver.configResponseMessage.ResponseMessage;
import com.example.orderserver.dto.OrderDto;
import com.example.orderserver.dto.ResponseDto;
import com.example.orderserver.models.Order;
import com.example.orderserver.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
@EnableCaching
@RateLimiter(name = "orderService")
public class OrderController {
    private OrderService orderService;
    private static int count = 1;
    private static boolean addMessage = false; // Biến đánh dấu để xác định khi nào cần thêm message vào ResponseDto

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody Order order, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Order savedOrder = orderService.saveOrder(order);
        if (savedOrder == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        }
    }

    //Hello

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#orderId", value = "ORDER")
    public ResponseEntity deleteOrder(@PathVariable("id") Long orderId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok(new ResponseMessage("Xóa hóa đơn thành công"));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Order> updateOrder(@RequestBody Order order, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        Order updatedOrder = orderService.updateOrder(order);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @GetMapping("/order-Circuit/{id}")
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCircuitServerOrder")
    public ResponseDto getOrder(@PathVariable("id") Long orderId, @RequestHeader("loggedInUser") String userName) {
        addMessage = true;
        ResponseDto responseDto = orderService.getOrder(orderId, userName);
        responseDto.setMessage("Thành công !");
        return responseDto;
    }

    @GetMapping("/order-RateSever/{id}")
    public ResponseDto getOrderRateLimiter(@PathVariable("id") Long orderId, @RequestHeader("loggedInUser") String userName) {
        addMessage = true;
        ResponseDto responseDto = orderService.getOrder(orderId, userName);
        responseDto.setMessage("Thành công !");
        return responseDto;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/order-Retry")
    @Retry(name = "orderService", fallbackMethod = "fallbackRetryServerOrder")
    public ResponseEntity<ResponseDto> getOrderByOrderIdAndUserId( @RequestParam("idOrder") Long orderId,
                                                                   @RequestParam("idUser") Long userId,
                                                                   @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        System.out.println("======================== RETRY =========================");
        System.out.println("Retry method called "+ count++ +" times at" + new Date());
        System.out.println("========================================================");
        addMessage = true;
        System.out.println("Logged in user details: " + userName);
        ResponseDto orders = orderService.getOrderByOrderIdAndUserId(orderId, userId, userName);
        orders.setMessage("Thành công !");
        return ResponseEntity.ok(orders);
    }

    public ResponseDto fallbackCircuitServerOrder(Long orderId, String userName, Exception e){
        System.out.println("=================================================CIRCUIT BREAKER===============================================");
        System.out.println("Fallback occurred. Error: " + e.getMessage());
        System.out.println("Order ID: " + orderId);
        System.out.println("User Name: " + userName);
        System.out.println("===============================================================================================================");
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("[CircuitBreaker - OrderServer] Product Service or User Service is not available.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto).getBody();
    }
    public ResponseEntity<ResponseDto> fallbackRetryServerOrder(Long orderId, Long userId, String userName, Throwable e) {
        System.out.println("=================================================RETRY FINISHED ========================================================");
        System.out.println("Fallback occurred. Error: " + e.getMessage());
        System.out.println("Order ID: " + orderId);
        System.out.println("User ID: " + userId);
        System.out.println("User Name: " + userName);
        System.out.println("===============================================================================================================");
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("[Retry - OrderServer] Order Server's callback time has expired (call once every 5 seconds) for a total of 5 calls.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

}
