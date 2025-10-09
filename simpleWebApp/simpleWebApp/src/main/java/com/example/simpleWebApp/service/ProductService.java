package com.example.simpleWebApp.service;

import com.example.simpleWebApp.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    List<Product> products= new ArrayList<>(Arrays.asList(new Product(101,"iphone",50000),new Product(102,"lenovo",60000)));
    public List<Product> getProducts(){
return products;
    }

   public Product getProductById(int prodId) {
       return products.stream().filter(p->p.getProdId()==prodId).findFirst().get();
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
    products.add(prod);
}

public void updateProduct(Product prod){
    int index=0;
    for(int i=0;i< products.size();i++){
        if(products.get(i).getProdId()== prod.getProdId()){
            index=i;
        }
products.set(index,prod);
    }
}

    public void deleteProduct(int prodId) {
        int index=0;
        for(int i=0;i< products.size();i++){
            if(products.get(i).getProdId()== prodId){
                index=i;
            }
            products.remove(index);
    }
}}