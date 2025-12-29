package com.example.ordermanagement.modules.delivery.mapper;

import com.example.ordermanagement.modules.delivery.dto.DeliveryPartnerResponse;
import com.example.ordermanagement.common.domain.Address;
import com.example.ordermanagement.modules.delivery.domain.model.DeliveryPartner;
import com.example.ordermanagement.common.domain.AddressEmbeddable;
import com.example.ordermanagement.modules.delivery.domain.entity.DeliveryPartnerEntity;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPartnerMapper {

    public DeliveryPartnerEntity toEntity(DeliveryPartner deliveryPartner) {
        if (deliveryPartner == null) return null;

        return DeliveryPartnerEntity.builder()
            .id(deliveryPartner.getId())
            .name(deliveryPartner.getName())
            .phoneNumber(deliveryPartner.getPhoneNumber())
            .email(deliveryPartner.getEmail())
            .status(deliveryPartner.getStatus())
            .currentLocation(toAddressEmbeddable(deliveryPartner.getCurrentLocation()))
            .vehicleType(deliveryPartner.getVehicleType())
            .vehicleNumber(deliveryPartner.getVehicleNumber())
            .assignedOrderCount(deliveryPartner.getAssignedOrderCount())
            .isActive(deliveryPartner.getIsActive())
            .build();
    }

    public DeliveryPartner toDomain(DeliveryPartnerEntity entity) {
        if (entity == null) return null;

        return DeliveryPartner.builder()
            .id(entity.getId())
            .name(entity.getName())
            .phoneNumber(entity.getPhoneNumber())
            .email(entity.getEmail())
            .status(entity.getStatus())
            .currentLocation(toAddress(entity.getCurrentLocation()))
            .vehicleType(entity.getVehicleType())
            .vehicleNumber(entity.getVehicleNumber())
            .assignedOrderCount(entity.getAssignedOrderCount())
            .isActive(entity.getIsActive())
            .build();
    }

    public DeliveryPartnerResponse toResponse(DeliveryPartner partner) {
        return DeliveryPartnerResponse.builder()
            .id(partner.getId())
            .name(partner.getName())
            .phoneNumber(partner.getPhoneNumber())
            .email(partner.getEmail())
            .status(partner.getStatus())
            .currentCity(partner.getCurrentLocation() != null ?
                partner.getCurrentLocation().getCity() : null)
            .vehicleType(partner.getVehicleType())
            .vehicleNumber(partner.getVehicleNumber())
            .assignedOrderCount(partner.getAssignedOrderCount())
            .isActive(partner.getIsActive())
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
}