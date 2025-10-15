package com.example.simpleWebApp.repo;

import com.example.simpleWebApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {
}