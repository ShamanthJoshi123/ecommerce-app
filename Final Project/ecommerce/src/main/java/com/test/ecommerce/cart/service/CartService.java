package com.test.ecommerce.cart.service;

import com.test.ecommerce.cart.model.Cart;

public interface CartService {

    Cart addToCart(Long userId, Long productId, int quantity);

    Cart getCart(Long userId);

    Cart removeFromCart(Long userId, Long productId);

    Cart updateCartItem(Long userId, Long productId, int quantity);


}