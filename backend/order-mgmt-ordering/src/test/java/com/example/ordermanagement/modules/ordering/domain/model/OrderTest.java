package com.example.ordermanagement.modules.ordering.domain.model;

import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("Should transition from INITIATED to PENDING_PAYMENT")
    void testInitialTransition() {
        Order order = Order.builder().status(OrderStatus.INITIATED).build();
        order.updateStatus(OrderStatus.PENDING_PAYMENT);
        assertEquals(OrderStatus.PENDING_PAYMENT, order.getStatus());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw exception for invalid transition from INITIATED")
    void testInvalidInitialTransition() {
        Order order = Order.builder().status(OrderStatus.INITIATED).build();
        assertThrows(IllegalStateException.class, () -> order.updateStatus(OrderStatus.PAID));
    }

    @Test
    @DisplayName("Should transition through happy path to DELIVERED")
    void testHappyPath() {
        Order order = Order.builder().status(OrderStatus.INITIATED).build();
        
        order.updateStatus(OrderStatus.PENDING_PAYMENT);
        order.updateStatus(OrderStatus.PAID);
        order.updateStatus(OrderStatus.CONFIRMED);
        order.updateStatus(OrderStatus.PREPARING);
        order.updateStatus(OrderStatus.READY_FOR_PICKUP);
        order.updateStatus(OrderStatus.PICKED_UP);
        order.updateStatus(OrderStatus.IN_TRANSIT);
        order.updateStatus(OrderStatus.DELIVERED);
        
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertNotNull(order.getDeliveredAt());
    }

    @Test
    @DisplayName("Should allow cancellation from allowed states")
    void testCancellation() {
        Set<OrderStatus> cancellableStates = Set.of(
            OrderStatus.PENDING_PAYMENT, 
            OrderStatus.PAID, 
            OrderStatus.CONFIRMED
        );

        for (OrderStatus state : cancellableStates) {
            Order order = Order.builder().status(state).build();
            order.cancel("Customer changed mind");
            assertEquals(OrderStatus.CANCELLED, order.getStatus());
            assertEquals("Customer changed mind", order.getCancellationReason());
            assertNotNull(order.getCancelledAt());
        }
    }

    @Test
    @DisplayName("Should not allow assignment if not READY_FOR_PICKUP")
    void testInvalidAssignment() {
        Order order = Order.builder().status(OrderStatus.CONFIRMED).build();
        assertThrows(IllegalStateException.class, () -> order.assignToDeliveryPartner(1L));
    }

    @Test
    @DisplayName("Should allow assignment when READY_FOR_PICKUP")
    void testValidAssignment() {
        Order order = Order.builder().status(OrderStatus.READY_FOR_PICKUP).build();
        order.assignToDeliveryPartner(123L);
        assertEquals(123L, order.getDeliveryPartnerId());
        assertNotNull(order.getAssignedAt());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"DELIVERED", "REFUNDED"})
    @DisplayName("Should not allow any transition from terminal states")
    void testTerminalStates(OrderStatus status) {
        Order order = Order.builder().status(status).build();
        assertThrows(IllegalStateException.class, () -> order.updateStatus(OrderStatus.INITIATED));
    }
}
