package com.test.ecommerce.order.controller;




import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.order.model.Order;
import com.test.ecommerce.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ApiResponse<Order> placeOrder(@RequestBody Order order) {
        return new ApiResponse<>("Order placed",
                orderService.placeOrder(order));
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
    public ApiResponse<Order> checkout(@PathVariable Long userId) {
        return new ApiResponse<>("Order placed from cart",
                orderService.placeOrderFromCart(userId));
    }
}