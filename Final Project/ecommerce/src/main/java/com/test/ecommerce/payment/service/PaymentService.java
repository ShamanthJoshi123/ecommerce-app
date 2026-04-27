package com.test.ecommerce.payment.service;
import com.test.ecommerce.payment.model.Payment;

public interface PaymentService {
    Payment processPayment(Long orderId, String method);

}

