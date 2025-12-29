package com.example.ordermanagement.modules.ordering.domain.repository;

import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByCustomerId(String customerId, Pageable pageable);
    Page<OrderEntity> findByRestaurantId(String restaurantId, Pageable pageable);
    Page<OrderEntity> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.deliveryAddress.city = :city AND o.status = :status")
    Page<OrderEntity> findByCityAndStatus(@Param("city") String city,
                                          @Param("status") OrderStatus status,
                                          Pageable pageable);

    List<OrderEntity> findByDeliveryPartnerId(Long deliveryPartnerId);

    @Query("SELECT o FROM OrderEntity o WHERE o.restaurantId = :restaurantId AND o.status IN :statuses")
    List<OrderEntity> findByRestaurantIdAndStatusIn(@Param("restaurantId") String restaurantId,
                                                    @Param("statuses") List<OrderStatus> statuses);
}