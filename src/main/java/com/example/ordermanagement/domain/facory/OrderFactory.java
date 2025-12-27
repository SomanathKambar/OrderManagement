package com.example.ordermanagement.domain.facory;

import com.example.ordermanagement.domain.enums.OrderStatus;
import com.example.ordermanagement.domain.enums.OrderType;
import com.example.ordermanagement.domain.enums.PaymentStatus;
import com.example.ordermanagement.domain.model.Address;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.model.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderFactory {

    public Order createOrder(String customerId, String customerName,
                             String restaurantId, String restaurantName,
                             OrderType orderType, Address deliveryAddress,
                             Address restaurantAddress, List<OrderItem> items,
                             String specialInstructions) {

        Order order = Order.builder()
                .customerId(customerId)
                .customerName(customerName)
                .restaurantId(restaurantId)
                .restaurantName(restaurantName)
                .orderType(orderType)
                .status(OrderStatus.CREATED)
                .paymentStatus(PaymentStatus.PENDING)
                .deliveryAddress(deliveryAddress)
                .restaurantAddress(restaurantAddress)
                .items(items != null ? items : new java.util.ArrayList<>())
                .deliveryCharge(calculateDeliveryCharge(orderType))
                .taxAmount(BigDecimal.valueOf(getTaxAmount()))
                .specialInstructions(specialInstructions)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        order.calculateGrandTotal();
        return order;
    }

    private Double getTaxAmount() {
//        ToDo make a helper to get actual tax until tax module changes added
        return 5.00;
    }

    private BigDecimal calculateDeliveryCharge(OrderType orderType) {
//        ToDo : Create a constant or get from a helper class and make it dynamic
        switch (orderType) {
            case GROCERY:
                return BigDecimal.valueOf(39.99);
            case PHARMACY:
                return BigDecimal.valueOf(19.99);
            case FOOD:
            default:
                return BigDecimal.valueOf(29.99);
        }
    }
}