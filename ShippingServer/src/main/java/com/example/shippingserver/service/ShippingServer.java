package com.example.shippingserver.service;

import com.example.shippingserver.models.Shipping;

import java.util.List;

public interface ShippingServer {
    Shipping saveShipping(Shipping shipping);

    void deleteShipping(Long id);

    Shipping updateShipping(Shipping shipping);

    Shipping getShippingById(Long id);

    List<Shipping> getAllShipping();
}
