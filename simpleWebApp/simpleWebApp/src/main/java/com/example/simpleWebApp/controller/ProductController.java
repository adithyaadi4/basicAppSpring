
package com.example.simpleWebApp.controller;
import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final int MAX_REQUESTS_PER_MIN = 10;
    private static final long WINDOW_DURATION_MS = 60_000; // 1 minute
    private int requestCount = 0;
    private long windowStart = Instant.now().toEpochMilli();

    private synchronized boolean allowRequest() {
        long now = Instant.now().toEpochMilli();

        // If more than 1 minute has passed, reset the window
        if (now - windowStart >= WINDOW_DURATION_MS) {
            requestCount = 0;
            windowStart = now;
        }

        // Check request count
        if (requestCount < MAX_REQUESTS_PER_MIN) {
            requestCount++;
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/products")
    public List<Product> getProducts(@RequestParam(defaultValue = "0") int page) {
        if (!allowRequest()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests — try again after a minute");
        }
        int size = 1000; // fetch 1000 records per request
        return productService.getPaginatedProducts(page, size);
    }

    @GetMapping("/products/{prodId}")
    public Product getProductById(@PathVariable int prodId) {
        return productService.getProductById(prodId);
    }

    @PostMapping("/products")
    public void addProduct(@RequestBody Product prod) {
        productService.addProduct(prod);
    }

    @PutMapping("/products")
    public void updateProduct(@RequestBody Product prod) {
        productService.updateProduct(prod);
    }

    @DeleteMapping("/products/{prodId}")
    public void deleteProduct(@PathVariable int prodId) {
        productService.deleteProduct(prodId);
    }
}

