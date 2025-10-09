package com.example.simpleWebApp.service;

import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
//    List<Product> products= new ArrayList<>(Arrays.asList(new Product(101,"iphone",50000),new Product(102,"lenovo",60000)));
    public List<Product> getProducts() {
        return productRepo.findAll();
    }
   public Product getProductById(int prodId) {
       return productRepo.findById(prodId).orElse(new Product(106,"apple",90000));
   }
   /*public Product getProductById(int prodId){
       for (Product p : products) {
          if (p.getProdId() == prodId) {
              return p; // return as soon as found
           }
      }
        return null; // return null if not found
   }*/
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