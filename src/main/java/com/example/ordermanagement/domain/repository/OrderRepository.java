package com.example.ordermanagement.domain.repository;


import com.example.ordermanagement.domain.enums.OrderStatus;
import com.example.ordermanagement.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;



public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    Page<Order> findByCustomerId(String customerId, Pageable pageable);
    Page<Order> findByRestaurantId(String restaurantId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByCityAndStatus(String city, OrderStatus status, Pageable pageable);
    List<Order> findByDeliveryPartnerId(Long deliveryPartnerId);
    List<Order> findByRestaurantIdAndStatusIn(String restaurantId, List<OrderStatus> statuses);
    Page<Order> findAll(Pageable pageable);
}