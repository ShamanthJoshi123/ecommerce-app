package com.test.ecommerce;

import com.test.ecommerce.order.model.Order;
import com.test.ecommerce.order.model.OrderItem;
import com.test.ecommerce.order.repository.OrderRepository;
import com.test.ecommerce.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testPlaceOrder_TotalCalculation() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setPrice(500.0);
        item.setQuantity(2);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.placeOrder(order);

        assertEquals(1000.0, result.getTotalAmount());
        assertEquals("PLACED", result.getStatus());
        verify(orderRepository).save(any(Order.class));
    }
}