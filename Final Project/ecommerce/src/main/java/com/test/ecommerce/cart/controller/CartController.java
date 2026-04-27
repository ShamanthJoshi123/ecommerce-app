package com.test.ecommerce.cart.controller;

import com.test.ecommerce.cart.model.Cart;
import com.test.ecommerce.cart.service.CartService;
import com.test.ecommerce.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ApiResponse<Cart> addToCart(@RequestParam Long userId,
                                       @RequestParam Long productId,
                                       @RequestParam int quantity) {

        Cart cart = cartService.addToCart(userId, productId, quantity);
        return new ApiResponse<>("Item added to cart", cart);
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
    public String removeFromCart(@RequestParam Long userId,
                                 @RequestParam Long productId) {

        cartService.removeFromCart(userId, productId);
        return "Item removed";
    }
}