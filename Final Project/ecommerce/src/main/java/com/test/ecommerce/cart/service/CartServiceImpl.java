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
import java.util.Iterator;
import java.util.Optional;

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

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        if (quantity > 0 && product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {

            CartItem item = existing.get();

            int newQty = item.getQuantity() + quantity;

            if (newQty <= 0) {
                cart.getItems().remove(item);
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(newQty);
                cartItemRepository.save(item);
            }

        } else {

            if (quantity > 0) {
                CartItem item = new CartItem();
                item.setProductId(productId);
                item.setQuantity(quantity);
                item.setCart(cart);

                cart.getItems().add(item);
                cartItemRepository.save(item);
            }
        }

        return cartRepository.save(cart);
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

            Iterator<CartItem> iterator = cart.getItems().iterator();

            while (iterator.hasNext()) {
                CartItem item = iterator.next();

                if (item.getProductId().equals(productId)) {
                    iterator.remove(); // remove from cart list
                    cartItemRepository.delete(item);
                }
            }
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