package com.test.ecommerce.payment.service.impl;

import com.test.ecommerce.order.model.Order;
import com.test.ecommerce.order.repository.OrderRepository;
import com.test.ecommerce.payment.model.Payment;
import com.test.ecommerce.payment.repository.PaymentRepository;
import com.test.ecommerce.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import com.test.ecommerce.order.model.OrderItem;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Payment processPayment(Long orderId, String method) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getUserId() == null) {
            throw new RuntimeException("Invalid order: no user linked");
        }
        if (!"PLACED".equals(order.getStatus())) {
            throw new RuntimeException("Payment allowed only for PLACED orders");
        }

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(method);
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        order.setStatus("PAID");
        orderRepository.save(order);

        for (OrderItem item : order.getItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int updatedQuantity = product.getQuantity() - item.getQuantity();

            if (updatedQuantity < 0) {
                throw new RuntimeException("Insufficient stock during payment");
            }

            product.setQuantity(updatedQuantity);

            productRepository.save(product);
        }
        return paymentRepository.save(payment);
    }

}
