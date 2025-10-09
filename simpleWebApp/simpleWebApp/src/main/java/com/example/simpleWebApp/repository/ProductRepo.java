package com.example.simpleWebApp.repository;

import com.example.simpleWebApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {
}
