package com.example.productserver.service.impl;

import com.example.productserver.configResponseMessage.ProductNotFoundException;
import com.example.productserver.models.Product;
import com.example.productserver.repository.ProductRepository;
import com.example.productserver.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String REDIS_KEY = "PRODUCT";
    @Override
    public Product saveProduct(Product product) {
        System.out.println("Called saveWarehouse() into DB & Redis");
        try {
            productRepository.save(product);
            redisTemplate.opsForHash().put(REDIS_KEY, product.getId().toString(), product);
            return product;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        System.out.println("Called deleteProduct() into DB & Redis: " + productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        productRepository.delete(product);
        redisTemplate.opsForHash().delete(REDIS_KEY, productId.toString());
    }

    @Override
    public Product updateProduct(Product product) {
        Product existingProduct_db = productRepository.findById(product.getId()).get();
        if (existingProduct_db != null) {
            System.out.println("Called updateProduct() into DB : " + existingProduct_db);
            existingProduct_db.setName(product.getName());
            existingProduct_db.setDescription(product.getDescription());
            existingProduct_db.setPrice(product.getPrice());
            existingProduct_db.setStockQuantity(product.getStockQuantity());
            existingProduct_db.setCategory(product.getCategory());

            existingProduct_db = productRepository.save(existingProduct_db);

            System.out.println("Called updateProduct() into Redis : " + existingProduct_db);
            redisTemplate.delete(REDIS_KEY + "::" + existingProduct_db.getId().toString());
            redisTemplate.opsForHash().put(REDIS_KEY, existingProduct_db.getId().toString(), existingProduct_db);

        }
        return existingProduct_db;
    }

    @Override
    public Product getProductById(Long productId) {
        System.out.println("Called getProductById() from redis: " + productId);
        return (Product) redisTemplate.opsForHash().get(REDIS_KEY, productId.toString());
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products;
        products = redisTemplate.opsForHash().values(REDIS_KEY);
        return products;
    }
}
