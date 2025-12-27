package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.enums.DeliveryPartnerStatus;
import com.example.ordermanagement.domain.model.DeliveryPartner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DeliveryPartnerRepository {
    DeliveryPartner save(DeliveryPartner deliveryPartner);
    Optional<DeliveryPartner> findById(Long id);
    List<DeliveryPartner> findAvailableByCity(String city);
    List<DeliveryPartner> findByStatus(DeliveryPartnerStatus status);
    Page<DeliveryPartner> findAll(Pageable pageable);
}