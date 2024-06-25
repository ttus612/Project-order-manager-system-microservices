package com.example.orderserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {
    private Long id;
    private String orderDate;
    private String status;
    private String discount;
    private String note;
    private Double total;
    private UserDto user;
    private List<ProductDto> products;
}
