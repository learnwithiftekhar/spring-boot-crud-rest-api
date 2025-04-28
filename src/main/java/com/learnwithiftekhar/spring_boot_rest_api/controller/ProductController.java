package com.learnwithiftekhar.spring_boot_rest_api.controller;

import com.learnwithiftekhar.spring_boot_rest_api.exception.ResourceNotFoundException;
import com.learnwithiftekhar.spring_boot_rest_api.model.Product;
import com.learnwithiftekhar.spring_boot_rest_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found for this id :: " + id)
                );
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        return productService.getProductsByCategory(categoryName);
    }

    @GetMapping("/price/{maxPrice}")
    public List<Product> getProductsByMaxPrice(@PathVariable Double maxPrice) {
        return productService.getProductsByPriceLessThanEqual(maxPrice);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.getProductsByProductName(name);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product newProduct = productService.saveProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // PUT update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product productDetails) {
        Product product = productService.getProductById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found for this id :: " + id)
                );

        // Update the product fields
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());

        Product updatedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // DELETE a product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found for this id :: " + id)
                );
        productService.deleteProduct(product);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }

}
