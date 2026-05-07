package com.test.ecommerce;

import com.test.ecommerce.cart.model.Cart;
import com.test.ecommerce.cart.model.CartItem;
import com.test.ecommerce.cart.repository.CartItemRepository;
import com.test.ecommerce.cart.repository.CartRepository;
import com.test.ecommerce.cart.service.CartServiceImpl;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void testAddToCart_NewCart() {
        Long userId = 1L;
        Long productId = 5L;

        Product product = new Product();
        product.setId(productId);
        product.setQuantity(10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart result = cartService.addToCart(userId, productId, 2);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartItemRepository).save(any(CartItem.class));
    }
}