package com.test.ecommerce;

import com.test.ecommerce.order.model.Order;
import com.test.ecommerce.order.repository.OrderRepository;
import com.test.ecommerce.payment.model.Payment;
import com.test.ecommerce.payment.repository.PaymentRepository;
import com.test.ecommerce.payment.service.impl.PaymentServiceImpl;
import com.test.ecommerce.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testProcessPayment() {

        Long orderId = 1L;

        // ✅ Create USER (IMPORTANT)
        User user = new User();
        user.setId(1L);

        // ✅ Create ORDER (FULL OBJECT)
        Order order = new Order();
        order.setUserId(1L);              // 🔥 THIS IS THE REAL FIX
        order.setStatus("PLACED");
        order.setTotalAmount(200.0);

        // ✅ MOCK
        when(orderRepository.findById(any()))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        when(paymentRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // 🔥 CALL
        Payment result = paymentService.processPayment(orderId, "UPI");

        // ✅ ASSERT
        assertNotNull(result);
    }
}