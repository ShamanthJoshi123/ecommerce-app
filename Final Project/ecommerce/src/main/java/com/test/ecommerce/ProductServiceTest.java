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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void testAddProduct() {
        Product product = new Product();
        when(repository.save(any())).thenReturn(product);

        Product result = service.addProduct(1L,product);

        assertNotNull(result);
    }

    @Test
    void testGetProductById() {
        Product product = new Product();

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = service.getProductById(1L);

        assertNotNull(result);
    }
}
