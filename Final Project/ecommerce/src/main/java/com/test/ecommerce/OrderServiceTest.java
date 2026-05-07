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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testPlaceOrder() {

        // CREATE ORDER
        Order order = new Order();

        // CREATE ITEM
        OrderItem item = new OrderItem();
        item.setPrice(100.0);
        item.setQuantity(2);

        // IMPORTANT: link item → order
        item.setOrder(order);

        // ADD ITEMS
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        // MOCK SAVE
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // CALL METHOD
        Order result = orderService.placeOrder(order);

        // ASSERTIONS
        assertNotNull(result);
        assertEquals(200.0, result.getTotalAmount());
        assertEquals("PLACED", result.getStatus());
        assertNotNull(result.getOrderDate());

        // VERIFY SAVE CALLED
        verify(orderRepository, times(1)).save(order);
    }
}