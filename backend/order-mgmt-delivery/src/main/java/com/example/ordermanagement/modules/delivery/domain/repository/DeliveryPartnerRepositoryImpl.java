package com.example.ordermanagement.modules.delivery.domain.repository;

import com.example.ordermanagement.modules.delivery.domain.DeliveryPartnerStatus;
import com.example.ordermanagement.modules.delivery.domain.model.DeliveryPartner;
import com.example.ordermanagement.modules.delivery.domain.repository.DeliveryPartnerRepository;
import com.example.ordermanagement.modules.delivery.domain.entity.DeliveryPartnerEntity;
import com.example.ordermanagement.modules.delivery.domain.repository.DeliveryPartnerJpaRepository;
import com.example.ordermanagement.modules.delivery.mapper.DeliveryPartnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DeliveryPartnerRepositoryImpl implements DeliveryPartnerRepository {
    
    private final DeliveryPartnerJpaRepository jpaRepository;
    private final DeliveryPartnerMapper deliveryPartnerMapper;

    @Override
    public DeliveryPartner save(DeliveryPartner deliveryPartner) {
        DeliveryPartnerEntity entity = deliveryPartnerMapper.toEntity(deliveryPartner);
        DeliveryPartnerEntity savedEntity = jpaRepository.save(entity);
        return deliveryPartnerMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<DeliveryPartner> findById(Long id) {
        return jpaRepository.findById(id)
            .map(deliveryPartnerMapper::toDomain);
    }
    
    @Override
    public List<DeliveryPartner> findAvailableByCity(String city) {
        return jpaRepository.findAvailableByCity(city).stream()
            .map(deliveryPartnerMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<DeliveryPartner> findByStatus(DeliveryPartnerStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(deliveryPartnerMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<DeliveryPartner> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
            .map(deliveryPartnerMapper::toDomain);
    }

}