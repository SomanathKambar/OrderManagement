package com.example.ordermanagement.util;

import com.example.ordermanagement.controller.dto.response.OrderResponse;
import com.example.ordermanagement.domain.model.Address;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.model.OrderItem;
import com.example.ordermanagement.data.entity.AddressEmbeddable;
import com.example.ordermanagement.data.entity.OrderEntity;
import com.example.ordermanagement.data.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderEntity toEntity(Order order) {
        if (order == null) return null;

        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .customerName(order.getCustomerName())
                .restaurantId(order.getRestaurantId())
                .restaurantName(order.getRestaurantName())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .deliveryAddress(toAddressEmbeddable(order.getDeliveryAddress()))
                .restaurantAddress(toAddressEmbeddable(order.getRestaurantAddress()))
                .totalAmount(order.getTotalAmount())
                .deliveryCharge(order.getDeliveryCharge())
                .taxAmount(order.getTaxAmount())
                .grandTotal(order.getGrandTotal())
                .deliveryPartnerId(order.getDeliveryPartnerId())
                .estimatedDeliveryMinutes(order.getEstimatedDeliveryMinutes())
                .actualDeliveryMinutes(order.getActualDeliveryMinutes())
                .specialInstructions(order.getSpecialInstructions()) // Map specialInstructions
                .cancellationReason(order.getCancellationReason())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .assignedAt(order.getAssignedAt())
                .pickedAt(order.getPickedAt())
                .deliveredAt(order.getDeliveredAt())
                .cancelledAt(order.getCancelledAt())
                .build();
        orderEntity.setItems(toOrderItemEntities(order.getItems()));
        return orderEntity;
    }

    public Order toDomain(OrderEntity entity) {
        if (entity == null) return null;

        return Order.builder()
            .id(entity.getId())
            .customerId(entity.getCustomerId())
            .customerName(entity.getCustomerName())
            .restaurantId(entity.getRestaurantId())
            .restaurantName(entity.getRestaurantName())
            .orderType(entity.getOrderType())
            .status(entity.getStatus())
            .paymentStatus(entity.getPaymentStatus())
            .deliveryAddress(toAddress(entity.getDeliveryAddress()))
            .restaurantAddress(toAddress(entity.getRestaurantAddress()))
            .items(toOrderItems(entity.getItems()))
            .totalAmount(entity.getTotalAmount())
            .deliveryCharge(entity.getDeliveryCharge())
            .taxAmount(entity.getTaxAmount())
            .grandTotal(entity.getGrandTotal())
            .deliveryPartnerId(entity.getDeliveryPartnerId())
            .estimatedDeliveryMinutes(entity.getEstimatedDeliveryMinutes())
            .actualDeliveryMinutes(entity.getActualDeliveryMinutes())
            .specialInstructions(entity.getSpecialInstructions()) // Map specialInstructions
            .cancellationReason(entity.getCancellationReason())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .assignedAt(entity.getAssignedAt())
            .pickedAt(entity.getPickedAt())
            .deliveredAt(entity.getDeliveredAt())
            .cancelledAt(entity.getCancelledAt())
            .build();
    }

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .customerName(order.getCustomerName())
                .restaurantId(order.getRestaurantId())
                .restaurantName(order.getRestaurantName())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .deliveryPartnerId(order.getDeliveryPartnerId())
                .totalAmount(order.getTotalAmount())
                .deliveryCharge(order.getDeliveryCharge())
                .taxAmount(order.getTaxAmount())
                .grandTotal(order.getGrandTotal())
                .estimatedDeliveryMinutes(order.getEstimatedDeliveryMinutes())
                .actualDeliveryMinutes(order.getActualDeliveryMinutes())
                .specialInstructions(order.getSpecialInstructions())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .assignedAt(order.getAssignedAt())
                .pickedAt(order.getPickedAt())
                .deliveredAt(order.getDeliveredAt())
                .cancelledAt(order.getCancelledAt())
                .deliveryCity(order.getDeliveryAddress().getCity())
                .restaurantCity(order.getRestaurantAddress().getCity())
                .build();
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        if (address == null) return null;

        return AddressEmbeddable.builder()
            .streetAddress(address.getStreetAddress())
            .city(address.getCity())
            .state(address.getState())
            .postalCode(address.getPostalCode())
            .country(address.getCountry())
            .latitude(address.getLatitude())
            .longitude(address.getLongitude())
            .build();
    }

    private Address toAddress(AddressEmbeddable embeddable) {
        if (embeddable == null) return null;

        return Address.builder()
            .streetAddress(embeddable.getStreetAddress())
            .city(embeddable.getCity())
            .state(embeddable.getState())
            .postalCode(embeddable.getPostalCode())
            .country(embeddable.getCountry())
            .latitude(embeddable.getLatitude())
            .longitude(embeddable.getLongitude())
            .build();
    }

    private List<OrderItemEntity> toOrderItemEntities(List<OrderItem> items) {
        if (items == null) return null;

        return items.stream()
            .map(this::toOrderItemEntity)
            .collect(Collectors.toList());
    }

    private OrderItemEntity toOrderItemEntity(OrderItem item) {
        if (item == null) return null;

        return OrderItemEntity.builder()
            .itemId(item.getItemId())
            .itemName(item.getItemName())
            .quantity(item.getQuantity())
            .unitPrice(item.getUnitPrice())
            .build();
    }

    private List<OrderItem> toOrderItems(List<OrderItemEntity> entities) {
        if (entities == null) return null;

        return entities.stream()
            .map(this::toOrderItem)
            .collect(Collectors.toList());
    }

    private OrderItem toOrderItem(OrderItemEntity entity) {
        if (entity == null) return null;

        return OrderItem.builder()
            .itemId(entity.getItemId())
            .itemName(entity.getItemName())
            .quantity(entity.getQuantity())
            .unitPrice(entity.getUnitPrice())
            .build();
    }
}