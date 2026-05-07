package com.test.ecommerce;

import com.test.ecommerce.order.model.Order;
import com.test.ecommerce.order.model.OrderItem;
import com.test.ecommerce.order.repository.OrderRepository;
import com.test.ecommerce.payment.model.Payment;
import com.test.ecommerce.payment.repository.PaymentRepository;
import com.test.ecommerce.payment.service.impl.PaymentServiceImpl;
import com.test.ecommerce.product.model.Product;
import com.test.ecommerce.product.repository.ProductRepository;
import com.test.ecommerce.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void testProcessPayment() {

        Long orderId = 1L;


        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(1L);
        order.setStatus("PLACED");
        order.setTotalAmount(200.0);


        OrderItem item = new OrderItem();
        item.setProductId(10L);
        item.setQuantity(2);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);


        Product product = new Product();
        product.setId(10L);
        product.setQuantity(10);


        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(i -> i.getArgument(0));


        Payment result = paymentService.processPayment(orderId, "UPI");


        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(200.0, result.getAmount());


        assertEquals(8, product.getQuantity()); // 10 - 2
    }
}