package com.test.ecommerce.product.service;

import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.test.ecommerce.user.model.User;
import com.test.ecommerce.user.repository.UserRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    private final UserRepository userRepository;

    public ProductService(ProductRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Product addProduct(Long userId, Product product) {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only ADMIN can add products");
        }

        return repository.save(product);
    }

    public Product updatePartial(Long userId, Long id,
                                 String name, Double price, Integer quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only ADMIN can update products");
        }

        Product product = getProductById(id);

        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (quantity != null) product.setQuantity(quantity);

        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public Product updateProduct(Long UserId, Long id, Product product) {

        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImageUrl(product.getImageUrl());

        return repository.save(existingProduct);
    }

    public void deleteProduct(Long userId, Long id) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only ADMIN can delete products");
        }

        repository.deleteById(id);
    }
}