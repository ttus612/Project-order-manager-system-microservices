package com.example.productserver.service;

import com.example.productserver.models.Product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    void deleteProduct(Long productId);

    Product updateProduct(Product product);

    Product getProductById(Long productId);
    List<Product> getAllProducts();

}
