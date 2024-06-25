package com.example.shippingserver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "shippings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Shipping implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameCompanyShipping;
    private String emailCompanyShipping;
    private String phoneNumberCompanyShipping;
    private String addressCompanyShipping;
    private String city;
    private String zipCode;

}
