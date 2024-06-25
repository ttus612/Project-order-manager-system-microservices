package com.example.orderserver.service;

import com.example.orderserver.dto.OrderDto;
import com.example.orderserver.dto.ResponseDto;
import com.example.orderserver.models.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    void deleteOrder(Long orderId);

    Order updateOrder(Order order);

    ResponseDto getOrder(Long orderId, String userName);

    List<Order> getAllOrders();

    ResponseDto getOrderByOrderIdAndUserId(Long orderId, Long userId, String userName);
}
