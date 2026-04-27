package com.test.ecommerce.product.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<Product> addProduct(@RequestParam Long userId,
                                           @RequestBody Product product) {
        Product saved = service.addProduct(userId, product);
        return new ApiResponse<>("Product added successfully", saved);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@RequestParam Long userId,
                                              @PathVariable Long id,
                                              @RequestBody Product product) {
        Product updated = service.updateProduct(userId, id, product);
        return new ApiResponse<>("Product updated", updated);
    }
    @PatchMapping("/{id}")
    public ApiResponse<Product> updateProductPartial(
            @RequestParam Long userId,
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer quantity) {

        Product updated = service.updatePartial(userId, id, name, price, quantity);
        return new ApiResponse<>("Product updated", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(@RequestParam Long userId,
                                             @PathVariable Long id) {
        service.deleteProduct(userId, id);
        return new ApiResponse<>("Product deleted", null);
    }
}