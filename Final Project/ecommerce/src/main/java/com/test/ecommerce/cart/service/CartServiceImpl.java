package com.test.ecommerce.cart.service;

import com.test.ecommerce.cart.model.Cart;
import com.test.ecommerce.cart.model.CartItem;
import com.test.ecommerce.cart.repository.CartItemRepository;
import com.test.ecommerce.cart.repository.CartRepository;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import com.test.ecommerce.user.model.User;
import com.test.ecommerce.user.repository.UserRepository;
import com.test.ecommerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Override
    public Cart addToCart(Long userId, Long productId, int quantity) {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


       if (!userService.isUserLoggedIn(userId)) {
            throw new RuntimeException("User not logged in");
       }

        // Check product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check quantity
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        // Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setItems(new ArrayList<>());
                    return newCart;
                });

        // Ensure list not null
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        // Create item
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
       //item.setPrice(product.getPrice());
        item.setCart(cart);

        cart.getItems().add(item);

        cartRepository.save(cart);
        cartItemRepository.save(item);

        return cart;
    }


    @Override
    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }


    @Override
    public Cart removeFromCart(Long userId, Long productId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems() != null) {
            cart.getItems().removeIf(item ->
                    item.getProductId().equals(productId));
        }

        return cartRepository.save(cart);
    }


    @Override
    public Cart updateCartItem(Long userId, Long productId, int quantity) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems() == null) {
            throw new RuntimeException("Cart is empty");
        }

        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
            }
        }

        return cartRepository.save(cart);
    }
}