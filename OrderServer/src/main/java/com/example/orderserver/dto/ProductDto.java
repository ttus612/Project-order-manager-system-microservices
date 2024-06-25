package com.example.orderserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String category;
}
