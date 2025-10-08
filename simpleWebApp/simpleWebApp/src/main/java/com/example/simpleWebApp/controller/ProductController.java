package com.example.simpleWebApp.controller;

import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class ProductController {
    @Autowired
    ProductService productService;
   @GetMapping("/products")
    public List<Product> getProducts(){
        return productService.getProducts();
    }
    @GetMapping("/products/{prodId}")
    public Product getProductById(@PathVariable int  prodId){
       return productService.getProductById(prodId);
    }
    @PostMapping("/products")
    public void addProduct(@RequestBody  Product prod){
       System.out.println(prod);
       productService.addProduct(prod);
    }
}
