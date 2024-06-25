package com.example.shippingserver.service.impl;

import com.example.shippingserver.configResponseMessage.ShippingNotFoundException;
import com.example.shippingserver.models.Shipping;
import com.example.shippingserver.repository.ShippingRepository;
import com.example.shippingserver.service.ShippingServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
@Slf4j
public class ShippingServerImpl implements ShippingServer {
    private ShippingRepository shippingRepository;

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String REDIS_KEY = "SHIPPING";
    @Override
    public Shipping saveShipping(Shipping shipping) {
//        return shippingRepository.save(shipping);
        System.out.println("Called saveShipping() into DB & Redis");
        try{
            shippingRepository.save(shipping);
            redisTemplate.opsForHash().put(REDIS_KEY, shipping.getId().toString(), shipping);
            return shipping;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteShipping(Long shippingId) {
        System.out.println("Called deleteSupplier() from DB & redis: " + shippingId);
        Shipping shipping = shippingRepository.findById(shippingId).orElseThrow(() -> new ShippingNotFoundException(shippingId));
        shippingRepository.delete(shipping);
        redisTemplate.opsForHash().delete(REDIS_KEY, shippingId.toString());
    }

    @Override
    public Shipping updateShipping(Shipping shipping) {
        Shipping existingShipping = shippingRepository.findById(shipping.getId()).orElse(null);
        if (existingShipping != null) {
            System.out.println("Called updateShipping() into DB & redis: " + existingShipping);
            existingShipping.setNameCompanyShipping(shipping.getNameCompanyShipping());
            existingShipping.setEmailCompanyShipping(shipping.getEmailCompanyShipping());
            existingShipping.setPhoneNumberCompanyShipping(shipping.getPhoneNumberCompanyShipping());
            existingShipping.setAddressCompanyShipping(shipping.getAddressCompanyShipping());
            existingShipping.setCity(shipping.getCity());
            existingShipping.setZipCode(shipping.getZipCode());

            existingShipping = shippingRepository.save(existingShipping);

            System.out.println("Called updateShipping() into Redis : " + existingShipping);
            redisTemplate.delete(REDIS_KEY + "::" + existingShipping.getId().toString());
            redisTemplate.opsForHash().put(REDIS_KEY, existingShipping.getId().toString(), existingShipping);
        }
        return existingShipping;
    }

    @Override
    public Shipping getShippingById(Long shippingId) {
        System.out.println("Called getShippingById() redis: " + shippingId);
        return (Shipping) redisTemplate.opsForHash().get(REDIS_KEY, shippingId.toString());
    }

    @Override
    public List<Shipping> getAllShipping() {
        List<Shipping> shippingList = (List<Shipping>) redisTemplate.opsForHash().values(REDIS_KEY);
        return shippingList;
    }
}
