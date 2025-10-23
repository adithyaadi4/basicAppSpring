package com.example.simpleWebApp.service;

import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product1 = new Product(101, "Laptop", 60000);
        product2 = new Product(102, "Phone", 30000);
    }

    @Test
    void testGetProducts() {
        List<Product> mockProducts = Arrays.asList(product1, product2);
        when(productRepo.findAll()).thenReturn(mockProducts);

        List<Product> products = productService.getProducts();

        assertEquals(2, products.size());
        assertEquals("Laptop", products.get(0).getProdName());
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void testGetProductById_WhenFound() {
        when(productRepo.findById(101)).thenReturn(Optional.of(product1));
        Product found = productService.getProductById(101);
        assertEquals("Laptop", found.getProdName());
        verify(productRepo, times(1)).findById(101);
    }

    @Test
    void testGetProductById_WhenNotFound() {
        when(productRepo.findById(999)).thenReturn(Optional.empty());
        Product result = productService.getProductById(999);
        assertEquals("apple", result.getProdName());
    }

    @Test
    void testAddProduct() {
        productService.addProduct(product1);
        verify(productRepo, times(1)).save(product1);
    }

    @Test
    void testUpdateProduct() {
        productService.updateProduct(product2);
        verify(productRepo, times(1)).save(product2);
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(101);
        verify(productRepo, times(1)).deleteById(101);
    }
}
