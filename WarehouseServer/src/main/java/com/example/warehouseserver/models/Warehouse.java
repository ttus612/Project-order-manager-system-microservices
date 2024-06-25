package com.example.warehouseserver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "warehouses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private int capacity;
    private String managerName;
    private String managerEmail;
    private String phoneNumber;
}
