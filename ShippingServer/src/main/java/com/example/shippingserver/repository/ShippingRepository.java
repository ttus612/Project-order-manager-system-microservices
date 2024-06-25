package com.example.shippingserver.repository;

import com.example.shippingserver.models.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
}
