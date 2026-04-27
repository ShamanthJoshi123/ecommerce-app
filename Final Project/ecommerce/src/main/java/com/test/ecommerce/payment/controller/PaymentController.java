package com.test.ecommerce.payment.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.payment.model.Payment;
import com.test.ecommerce.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{orderId}")
    public ApiResponse<Payment> makePayment(@PathVariable Long orderId,
                                            @RequestParam String method) {

        Payment payment = paymentService.processPayment(orderId, method);
        return new ApiResponse<>("Payment successful", payment);
    }
}