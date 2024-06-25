package com.example.orderserver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderDate;
    private String status;
    private String discount;
    private String note;
    private Double total;
    private Long userId;
    @ElementCollection
    private List<Long> productIds;
//    private Long productId;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", discount='" + discount + '\'' +
                ", note='" + note + '\'' +
                ", total=" + total +
                ", userId=" + userId +
                ", productIds=" + productIds +
                '}';
    }
}
