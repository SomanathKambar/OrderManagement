
package com.example.ordermanagement.modules.ordering.domain.repository;

import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.model.Order;
import com.example.ordermanagement.modules.ordering.domain.repository.OrderRepository;
import com.example.ordermanagement.modules.ordering.domain.entity.OrderEntity;
import com.example.ordermanagement.modules.ordering.domain.repository.OrderJpaRepository;
import com.example.ordermanagement.modules.ordering.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderMapper.toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return orderMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
                .map(orderMapper::toDomain);
    }

    @Override
    public Page<Order> findByCustomerId(String customerId, Pageable pageable) {
        return jpaRepository.findByCustomerId(customerId, pageable)
                .map(orderMapper::toDomain);
    }

    @Override
    public Page<Order> findByRestaurantId(String restaurantId, Pageable pageable) {
        return jpaRepository.findByRestaurantId(restaurantId, pageable)
                .map(orderMapper::toDomain);
    }

    @Override
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
                .map(orderMapper::toDomain);
    }

    @Override
    public Page<Order> findByCityAndStatus(String city, OrderStatus status, Pageable pageable) {
        return jpaRepository.findByCityAndStatus(city, status, pageable)
                .map(orderMapper::toDomain);
    }

    @Override
    public List<Order> findByDeliveryPartnerId(Long deliveryPartnerId) {
        return jpaRepository.findByDeliveryPartnerId(deliveryPartnerId).stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByRestaurantIdAndStatusIn(String restaurantId, List<OrderStatus> statuses) {
        return jpaRepository.findByRestaurantIdAndStatusIn(restaurantId, statuses).stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(orderMapper::toDomain);
    }

}