package com.test.ecommerce;

import com.test.ecommerce.cart.model.Cart;
import com.test.ecommerce.cart.repository.CartItemRepository;
import com.test.ecommerce.cart.repository.CartRepository;
import com.test.ecommerce.cart.service.CartServiceImpl;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import com.test.ecommerce.user.model.User;
import com.test.ecommerce.user.repository.UserRepository;
import com.test.ecommerce.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void testAddToCart() {

        Long userId = 1L;
        Long productId = 1L;


        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));


        when(userService.isUserLoggedIn(userId)).thenReturn(true);


        Product product = new Product();
        product.setId(productId);
        product.setQuantity(10);
        product.setPrice(100.0);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));


        when(cartRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        when(cartRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        when(cartItemRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);


        Cart result = cartService.addToCart(userId, productId, 2);


        assertNotNull(result);
        verify(cartRepository).save(any());
    }
}
