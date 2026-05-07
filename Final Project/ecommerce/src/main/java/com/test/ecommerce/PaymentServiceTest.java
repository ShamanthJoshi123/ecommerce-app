package com.test.ecommerce;

import com.test.ecommerce.order.model.Order;
import com.test.ecommerce.order.model.OrderItem;
import com.test.ecommerce.order.repository.OrderRepository;
import com.test.ecommerce.payment.model.Payment;
import com.test.ecommerce.payment.repository.PaymentRepository;
import com.test.ecommerce.payment.service.impl.PaymentServiceImpl;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testProcessPayment_Success() {
        Long orderId = 101L;
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(1L);
        order.setTotalAmount(1500.0);
        order.setStatus("PLACED");

        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setQuantity(1);
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        Payment result = paymentService.processPayment(orderId, "UPI");

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("PAID", order.getStatus());
        assertEquals(9, product.getQuantity()); // Stock reduced
    }
}