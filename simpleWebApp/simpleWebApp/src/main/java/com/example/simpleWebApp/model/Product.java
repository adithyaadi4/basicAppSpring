package com.example.simpleWebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Product {
    private Integer prodId;   // use Integer instead of int
    private String prodName;
    private Integer price;

    public int getProdId() {
        return prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public int getPrice() {
        return price;
    }

    public Product(int prodId, String prodName, int price) {
        this.prodId = prodId;
        this.price = price;
        this.prodName = prodName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "prodId=" + prodId +
                ", prodName='" + prodName + '\'' +
                ", price=" + price +
                '}';
    }
}
