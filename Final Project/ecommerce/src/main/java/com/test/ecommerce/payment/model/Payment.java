package com.test.ecommerce.payment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.ecommerce.order.model.Order;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

@Table(name = "payments")

public class Payment {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long paymentId;

    private Double amount;

    private String paymentMethod; // CARD / UPI / COD

    private String status; // SUCCESS / FAILED / PENDING

    private LocalDateTime paymentDate;  // LOCAL TIME AND DATE

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
