package com.test.ecommerce.order.controller;




import com.razorpay.RazorpayClient;
import com.test.ecommerce.cart.model.Cart;
import com.test.ecommerce.cart.model.CartItem;
import com.test.ecommerce.cart.repository.CartRepository;
import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.order.dto.CheckoutRequest;
import com.test.ecommerce.order.model.Order;
import com.test.ecommerce.order.service.OrderService;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
   @Autowired
   private CartRepository cartRepository;
   @Autowired
   private ProductRepository productRepository;
    @PostMapping("/create-payment")
    public Map<String, Object> createPayment(@RequestParam Long userId) {

        try {

            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            if (cart.getItems() == null || cart.getItems().isEmpty()) {
                throw new RuntimeException("Cart is empty");
            }

            double total = 0;

            for (CartItem item : cart.getItems()) {

                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                total += product.getPrice() * item.getQuantity();
            }


            double discount = 0;
            if (total > 50000) {
                discount = 2000;
            }

            double finalAmount = total - discount;

            RazorpayClient client = new RazorpayClient("rzp_test_SlEnNKNbyoYMJs", "Ckwgp4j21ko8ZiID3kYW2bTQ");

            JSONObject options = new JSONObject();
            options.put("amount", (int)(finalAmount * 100));
            options.put("currency", "INR");
            options.put("receipt", "txn_" + userId);

            com.razorpay.Order razorOrder = client.orders.create(options);

            Map<String, Object> res = new HashMap<>();
            res.put("orderId", razorOrder.get("id"));
            res.put("amount", razorOrder.get("amount"));

            return res;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Payment creation failed: " + e.getMessage());
        }
    }


    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }


    @GetMapping("/{id}")
    public ApiResponse<Order> getOrder(@PathVariable Long id) {
        return new ApiResponse<>("Order fetched",
                orderService.getOrderById(id));
    }

    @PostMapping("/checkout/{userId}")
    public ApiResponse<Order> checkout(
            @PathVariable Long userId,
            @RequestBody CheckoutRequest req) {

        return new ApiResponse<>("Order placed",
                orderService.placeOrderFromCart(userId, req));
    }
}