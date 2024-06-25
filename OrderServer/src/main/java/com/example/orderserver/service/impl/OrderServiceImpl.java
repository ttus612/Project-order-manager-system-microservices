package com.example.orderserver.service.impl;

import com.example.orderserver.configResponseMessage.OrderNotFoundException;
import com.example.orderserver.dto.OrderDto;
import com.example.orderserver.dto.ProductDto;
import com.example.orderserver.dto.ResponseDto;
import com.example.orderserver.dto.UserDto;
import com.example.orderserver.models.Order;
import com.example.orderserver.repository.OrderRepository;
import com.example.orderserver.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String REDIS_KEY = "ORDER";
    @Override
    public Order saveOrder(Order order) {
        System.out.println("Called saveOrDer() into DB & Redis");
        try {
            orderRepository.save(order);
            redisTemplate.opsForHash().put(REDIS_KEY, order.getId().toString(), order);
            return order;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        System.out.println("Called deleteOrder() into DB & Redis: "+ orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        orderRepository.delete(order);
        redisTemplate.opsForHash().delete(REDIS_KEY, orderId.toString());
    }

    @Override
    public Order updateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getId()).get();
        if (existingOrder != null) {
            System.out.println("Called updateOrder() into DB & Redis: " + existingOrder.getId());
            existingOrder.setOrderDate(order.getOrderDate());
            existingOrder.setStatus(order.getStatus());
            existingOrder.setDiscount(order.getDiscount());
            existingOrder.setNote(order.getNote());
            existingOrder.setTotal(order.getTotal());
            existingOrder.setProductIds(order.getProductIds());

            existingOrder = orderRepository.save(existingOrder);

            System.out.println("Called updateOrder() into Redis: " + existingOrder);
            redisTemplate.delete(REDIS_KEY + "::" + existingOrder.getId().toString());
            redisTemplate.opsForHash().put(REDIS_KEY, existingOrder.getId().toString(), order);
        }
        return existingOrder;
    }

    @Override
    public ResponseDto getOrder(Long orderId, String userName) {
//        String cacheKey = "ORDER::" + orderId;
//        ResponseDto cachedResponse = (ResponseDto) redisTemplate.opsForHash().get(REDIS_KEY, cacheKey);
//        if (cachedResponse != null) {
//            return cachedResponse;
//        }
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderDto orderDto = mapToOrder(order);

        System.out.println("Ussername: " + userName);
        // Tạo HttpHeaders và thêm thông tin người dùng vào tiêu đề
        HttpHeaders headers = new HttpHeaders();
        headers.set("loggedInUser", userName);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<UserDto> userResponse = restTemplate
                .exchange("http://user-server:8082/api/users/" + order.getUserId(),
                        HttpMethod.GET,entity,
                        UserDto.class);
        UserDto userDto = userResponse.getBody();
        System.out.println("Called Service User:" + userResponse.getStatusCode());
        orderDto.setUser(userDto);

        List<ProductDto> productDtoList = new ArrayList<>();
        for (Long productId : order.getProductIds()) {
            ResponseEntity<ProductDto> productResponse = restTemplate
                    .exchange("http://product-server:8083/api/products/" + productId,
                            HttpMethod.GET,
                            entity,
                            ProductDto.class);
            ProductDto productDto = productResponse.getBody();
            System.out.println("Called Service Product:" + productResponse.getStatusCode());
            productDtoList.add(productDto);
        }

        orderDto.setProducts(productDtoList);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setOrder(orderDto);
//        redisTemplate.opsForHash().get(REDIS_KEY, responseDto.getOrder().getId().toString());
        return responseDto;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders;
        orders = orderRepository.findAll();
//        orders = redisTemplate.opsForHash().values(REDIS_KEY);
        return orders;
    }

    @Override
    public ResponseDto getOrderByOrderIdAndUserId(Long orderId, Long userId, String userName) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderDto orderDto = mapToOrder(order);

        System.out.println("Ussername: " + userName);
        // Tạo HttpHeaders và thêm thông tin người dùng vào tiêu đề
        HttpHeaders headers = new HttpHeaders();
        headers.set("loggedInUser", userName);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<UserDto> userResponse = restTemplate
                .exchange("http://user-server:8082/api/users/" + userId,
                        HttpMethod.GET,entity,
                        UserDto.class);
        UserDto userDto = userResponse.getBody();
        System.out.println("Called Service User:" + userResponse.getStatusCode());
        orderDto.setUser(userDto);

        List<ProductDto> productDtoList = new ArrayList<>();
        for (Long productId : order.getProductIds()) {
            ResponseEntity<ProductDto> productResponse = restTemplate
                    .exchange("http://product-server:8083/api/products/" + productId,
                            HttpMethod.GET,
                            entity,
                            ProductDto.class);
            ProductDto productDto = productResponse.getBody();
            System.out.println("Called Service Product:" + productResponse.getStatusCode());
            productDtoList.add(productDto);
        }

        orderDto.setProducts(productDtoList);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setOrder(orderDto);
//        redisTemplate.opsForHash().get(REDIS_KEY, responseDto.getOrder().getId().toString());
        return responseDto;
    }

    private OrderDto mapToOrder(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setStatus(order.getStatus());
        orderDto.setDiscount(order.getDiscount());
        orderDto.setNote(order.getNote());
        orderDto.setTotal(order.getTotal());
        return orderDto;
    }
}
