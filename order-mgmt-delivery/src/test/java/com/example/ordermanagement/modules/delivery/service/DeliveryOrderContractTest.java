package com.example.ordermanagement.modules.delivery.service;

import com.example.ordermanagement.modules.delivery.domain.DeliveryPartnerStatus;
import com.example.ordermanagement.modules.delivery.domain.model.DeliveryPartner;
import com.example.ordermanagement.modules.delivery.domain.repository.DeliveryPartnerRepository;
import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.model.Order;
import com.example.ordermanagement.modules.ordering.exception.InvalidOrderStateException;
import com.example.ordermanagement.modules.ordering.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Delivery-Ordering Module Contract Test")
class DeliveryOrderContractTest {

    @Mock
    private OrderService orderService;

    @Mock
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @InjectMocks
    private DeliveryPartnerService deliveryPartnerService;

    @Test
    @DisplayName("Contract: Delivery module should be able to assign partner if order is CONFIRMED")
    void testAssignmentContract() {
        // Arrange
        Long orderId = 1L;
        Long partnerId = 100L;
        Order mockOrder = Order.builder()
                .id(orderId)
                .status(OrderStatus.CONFIRMED)
                .build();
        
        DeliveryPartner mockPartner = DeliveryPartner.builder()
                .id(partnerId)
                .status(DeliveryPartnerStatus.AVAILABLE)
                .assignedOrderCount(0)
                .isActive(true)
                .build();

        when(orderService.getOrder(orderId)).thenReturn(mockOrder);
        when(deliveryPartnerRepository.findById(partnerId)).thenReturn(Optional.of(mockPartner));
        when(deliveryPartnerRepository.save(any())).thenReturn(mockPartner);

        // Act
        deliveryPartnerService.assignToOrder(orderId, partnerId);

        // Assert - Verify that the delivery module calls the ordering module's assignment method
        verify(orderService).assignOrder(orderId, partnerId);
    }

    @Test
    @DisplayName("Contract: Delivery module should fail if OrderService throws InvalidOrderStateException")
    void testInvalidStateContract() {
        // Arrange
        Long orderId = 1L;
        Long partnerId = 100L;
        Order mockOrder = Order.builder()
                .id(orderId)
                .status(OrderStatus.DELIVERED) // Terminal state, definitely not assignable
                .build();
        
        DeliveryPartner mockPartner = DeliveryPartner.builder()
                .id(partnerId)
                .status(DeliveryPartnerStatus.AVAILABLE)
                .assignedOrderCount(0)
                .isActive(true)
                .build();

        when(orderService.getOrder(orderId)).thenReturn(mockOrder);
        when(deliveryPartnerRepository.findById(partnerId)).thenReturn(Optional.of(mockPartner));

        // Act & Assert
        assertThrows(InvalidOrderStateException.class, () -> {
            deliveryPartnerService.assignToOrder(orderId, partnerId);
        });
    }
}
