package com.test.ecommerce.cart.controller;

import com.test.ecommerce.cart.model.Cart;
import com.test.ecommerce.cart.service.CartService;
import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ApiResponse<?> addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        if (userId == null || !userRepository.existsById(userId)) {
            throw new RuntimeException("User not logged in");
        }

        return new ApiResponse<>("Added",
                cartService.addToCart(userId, productId, quantity));
    }

    @GetMapping("/{userId}")
    public ApiResponse<Cart> getCart(@PathVariable Long userId) {
        return new ApiResponse<>("Cart fetched", cartService.getCart(userId));
    }

    @PutMapping("/update")
    public Cart updateCart(@RequestParam Long userId,
                           @RequestParam Long productId,
                           @RequestParam int quantity) {

        return cartService.updateCartItem(userId, productId, quantity);
    }

    @DeleteMapping("/remove")
    public ApiResponse<Cart> removeItem(@RequestParam Long userId,
                                        @RequestParam Long productId) {

        Cart cart = cartService.removeFromCart(userId, productId);
        return new ApiResponse<>("Item removed", cart);
    }

}