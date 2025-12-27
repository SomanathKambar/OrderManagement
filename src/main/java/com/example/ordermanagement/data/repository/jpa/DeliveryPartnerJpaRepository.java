package com.example.ordermanagement.data.repository.jpa;

import com.example.ordermanagement.data.entity.DeliveryPartnerEntity;
import com.example.ordermanagement.domain.enums.DeliveryPartnerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryPartnerJpaRepository extends JpaRepository<DeliveryPartnerEntity, Long> {

    @Query("SELECT d FROM DeliveryPartnerEntity d WHERE d.currentLocation.city = :city " +
            "AND d.status = 'AVAILABLE' AND d.isActive = true " +
            "AND d.assignedOrderCount < 3")
    List<DeliveryPartnerEntity> findAvailableByCity(@Param("city") String city);

    List<DeliveryPartnerEntity> findByStatus(DeliveryPartnerStatus status);

    @Query("SELECT d FROM DeliveryPartnerEntity d WHERE d.isActive = true " +
            "ORDER BY d.assignedOrderCount ASC")
    List<DeliveryPartnerEntity> findAvailableOrderByLoad();
}