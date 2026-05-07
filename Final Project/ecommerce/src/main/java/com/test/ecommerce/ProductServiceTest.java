package com.test.ecommerce;

import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import com.test.ecommerce.product.service.ProductService;
import com.test.ecommerce.user.model.User;
import com.test.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testAddProduct_AsAdmin() {
        Long userId = 1L;
        User admin = new User();
        admin.setRole("ADMIN");

        Product product = new Product();
        product.setName("Laptop");

        when(userRepository.findById(userId)).thenReturn(Optional.of(admin));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(userId, product);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(10L);

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }
}