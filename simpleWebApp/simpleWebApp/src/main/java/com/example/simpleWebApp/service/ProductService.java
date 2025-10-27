package com.example.simpleWebApp.service;

import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    public List<Product> getProducts() {
        return productRepo.findAll();
    }
    public Product getProductById(int prodId) {
        return productRepo.findById(prodId).orElse(new Product(106,"apple",90000));
    }
    public List<Product> getPaginatedProducts(int page, int size) {
        return productRepo.findAll(PageRequest.of(page, size)).getContent();
    }

    public void addProduct(Product prod){
        productRepo.save(prod);
    }

    public void updateProduct(Product prod){
        productRepo.save(prod);
    }

    public void deleteProduct(int prodId) {
        productRepo.deleteById(prodId);
    }
}