package com.test.ecommerce;

import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import com.test.ecommerce.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetProductById() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Phone");
        product.setPrice(10000.0);
        product.setQuantity(10);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals("Phone", result.getName());
    }
}