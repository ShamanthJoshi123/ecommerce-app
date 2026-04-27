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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testPlaceOrder() {

        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setPrice(100.0);
        item.setQuantity(2);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.placeOrder(order);

        assertNotNull(result);
        assertEquals(200.0, result.getTotalAmount());
    }
}