package com.example.ordermanagement.modules.ordering.service;


import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.OrderType;
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

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;

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

        log.info("Creating order: {}", order.getId());
        return orderRepository.save(order);
    }

    @Cacheable(value = "orders", key = "#id")
    public Order getOrder(Long id) {
        log.debug("Fetching order: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + id));
    }

    @CacheEvict(value = "orders", key = "#id")
    public Order updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = getOrder(id);
        OrderStatus oldStatus = order.getStatus();

        try {
            order.updateStatus(newStatus);
            Order updatedOrder = orderRepository.save(order);

            log.info("Order {}: {} → {}", id, oldStatus, newStatus);
            return updatedOrder;

        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(
                    "Cannot transition order " + id + " from " +
                            oldStatus + " to " + newStatus + ": " + e.getMessage());
        }
    }

    @CacheEvict(value = "orders", key = "#id")
    public Order cancelOrder(Long id, String reason) {
        Order order = getOrder(id);
        order.cancel(reason);
        return orderRepository.save(order);
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
                List.of(OrderStatus.CREATED, OrderStatus.ASSIGNED, OrderStatus.PICKED)
        );
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}