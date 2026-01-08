package com.example.ordermanagement.modules.ordering.service;


import com.example.ordermanagement.common.event.*;
import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.OrderType;
import com.example.ordermanagement.modules.ordering.domain.event.OrderEventPublisher;
import com.example.ordermanagement.modules.ordering.domain.factory.OrderFactory;
import com.example.ordermanagement.common.domain.Address;
import com.example.ordermanagement.modules.ordering.domain.model.Order;
import com.example.ordermanagement.modules.ordering.domain.model.OrderItem;
import com.example.ordermanagement.modules.ordering.exception.InvalidOrderStateException;
import com.example.ordermanagement.modules.ordering.exception.OrderNotFoundException;
import com.example.ordermanagement.modules.ordering.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final OrderEventPublisher eventPublisher;

    public Order createOrder(String customerId, String customerName,
                             String restaurantId, String restaurantName,
                             String orderType, Address deliveryAddress,
                             Address restaurantAddress, List<OrderItem> items,
                             String specialInstructions) {

        Order order = orderFactory.createOrder(
                customerId, customerName, restaurantId, restaurantName,
                OrderType.valueOf(orderType), deliveryAddress, restaurantAddress,
                items, specialInstructions
        );

        Order savedOrder = orderRepository.save(order);
        log.info("Created order: {}", savedOrder.getId());

        eventPublisher.publish(OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(LocalDateTime.now())
                .orderId(savedOrder.getId())
                .customerId(customerId)
                .restaurantId(restaurantId)
                .amount(savedOrder.getGrandTotal().doubleValue())
                .build());

        return savedOrder;
    }

    @Cacheable(value = "orders", key = "#id")
    public Order getOrder(Long id) {
        log.debug("Fetching order: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + id));
    }

    @CacheEvict(value = "orders", key = "#id")
    public Order updateOrderStatus(Long id, OrderStatus newStatus, String reason, boolean dryRun) {
        Order order = getOrder(id);
        OrderStatus oldStatus = order.getStatus();

        try {
            order.updateStatus(newStatus);
            if (reason != null && !reason.isBlank()) {
               log.info("Order {} status update reason: {}", id, reason);
            }
            
            if (dryRun) {
                log.info("[DRY RUN] Validated transition for order {}: {} → {}", id, oldStatus, newStatus);
                return order; // Return validated object without saving
            }

            Order updatedOrder = orderRepository.save(order);

            log.info("Order {}: {} → {}", id, oldStatus, newStatus);

            String eventId = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now();

            OrderEvent event = switch (newStatus) {
                case PENDING_PAYMENT -> OrderInitiatedEvent.builder().eventId(eventId).occurredAt(now).orderId(id).build();
                case PAID -> OrderPaidEvent.builder().eventId(eventId).occurredAt(now).orderId(id).paymentId("PAY_" + id).build();
                case CONFIRMED -> OrderConfirmedEvent.builder().eventId(eventId).occurredAt(now).orderId(id).build();
                case PREPARING -> OrderPreparingEvent.builder().eventId(eventId).occurredAt(now).orderId(id).build();
                case READY_FOR_PICKUP -> OrderReadyEvent.builder().eventId(eventId).occurredAt(now).orderId(id).build();
                case PICKED_UP -> OrderPickedUpEvent.builder().eventId(eventId).occurredAt(now).orderId(id).build();
                case IN_TRANSIT -> OrderInTransitEvent.builder().eventId(eventId).occurredAt(now).orderId(id).build();
                case DELIVERED -> OrderCompletedEvent.builder().eventId(eventId).occurredAt(now).orderId(id).build();
                case FAILED -> OrderFailedEvent.builder().eventId(eventId).occurredAt(now).orderId(id).reason(reason).build();
                case CANCELLED -> OrderCancelledEvent.builder().eventId(eventId).occurredAt(now).orderId(id).reason(reason).build();
                case REFUNDED -> OrderRefundedEvent.builder().eventId(eventId).occurredAt(now).orderId(id).amount(updatedOrder.getGrandTotal().doubleValue()).build();
                default -> null;
            };

            if (event != null) {
                eventPublisher.publish(event);
            }

            return updatedOrder;

        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(
                    "Cannot transition order " + id + " from " +
                            oldStatus + " to " + newStatus + ": " + e.getMessage());
        }
    }

    @CacheEvict(value = "orders", key = "#id")
    public Order assignOrder(Long id, Long deliveryPartnerId) {
        Order order = getOrder(id);
        order.assignToDeliveryPartner(deliveryPartnerId);
        Order updatedOrder = orderRepository.save(order);

        eventPublisher.publish(OrderAssignedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(LocalDateTime.now())
                .orderId(id)
                .deliveryPartnerId(deliveryPartnerId)
                .build());

        return updatedOrder;
    }

    @CacheEvict(value = "orders", key = "#id")
    public Order cancelOrder(Long id, String reason) {
        Order order = getOrder(id);
        order.cancel(reason);
        Order savedOrder = orderRepository.save(order);

        eventPublisher.publish(OrderCancelledEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(LocalDateTime.now())
                .orderId(id)
                .reason(reason)
                .build());

        return savedOrder;
    }

    @CacheEvict(value = "orders", key = "#id")
    public Order refundOrder(Long id, String reason, boolean dryRun) {
        Order order = getOrder(id);
        
        // Strict check: Refund only allowed for FAILED or CANCELLED
        if (order.getStatus() != OrderStatus.FAILED && order.getStatus() != OrderStatus.CANCELLED) {
             throw new InvalidOrderStateException("Refund is allowed only when order is FAILED or CANCELLED. Current status: " + order.getStatus());
        }

        return updateOrderStatus(id, OrderStatus.REFUNDED, reason, dryRun);
    }

    @CacheEvict(value = "orders", key = "#id")
    public Order updateSpecialInstructions(Long id, String instructions) {
        Order order = getOrder(id);
        order.setSpecialInstructions(instructions);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public Page<Order> searchOrders(String customerId, String restaurantId,
                                    OrderStatus status, String city,
                                    Pageable pageable) {
        log.debug("Searching orders with filters - customerId: {}, restaurantId: {}, status: {}, city: {}",
                customerId, restaurantId, status, city);

        if (customerId != null) {
            return orderRepository.findByCustomerId(customerId, pageable);
        } else if (restaurantId != null) {
            return orderRepository.findByRestaurantId(restaurantId, pageable);
        } else if (status != null && city != null) {
            return orderRepository.findByCityAndStatus(city, status, pageable);
        } else if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }

    public List<Order> getOrdersByDeliveryPartner(Long partnerId) {
        return orderRepository.findByDeliveryPartnerId(partnerId);
    }

    public List<Order> getActiveOrdersByRestaurant(String restaurantId) {
        return orderRepository.findByRestaurantIdAndStatusIn(
                restaurantId,
                List.of(OrderStatus.INITIATED, OrderStatus.PENDING_PAYMENT, OrderStatus.PAID, OrderStatus.CONFIRMED, OrderStatus.PREPARING)
        );
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}