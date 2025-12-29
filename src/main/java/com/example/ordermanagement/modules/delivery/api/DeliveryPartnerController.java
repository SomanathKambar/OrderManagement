package com.example.ordermanagement.modules.delivery.api;

import com.example.ordermanagement.modules.delivery.dto.DeliveryPartnerResponse;
import com.example.ordermanagement.modules.delivery.domain.model.DeliveryPartner;
import com.example.ordermanagement.modules.delivery.service.DeliveryPartnerService;
import com.example.ordermanagement.modules.delivery.mapper.DeliveryPartnerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/delivery-partners")
@Tag(name = "Delivery Partners", description = "Delivery partner management endpoints")
@Slf4j
@RequiredArgsConstructor
public class DeliveryPartnerController {

    private final DeliveryPartnerService deliveryPartnerService;
    private final DeliveryPartnerMapper deliveryPartnerMapper;

    @GetMapping
    @Operation(summary = "Get all delivery partners with pagination")
    public ResponseEntity<Page<DeliveryPartnerResponse>> getAllPartners(Pageable pageable) {
        Page<DeliveryPartner> partners = deliveryPartnerService.getAllPartners(pageable);
        Page<DeliveryPartnerResponse> response = partners.map(deliveryPartnerMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get delivery partner by ID")
    public ResponseEntity<DeliveryPartnerResponse> getPartner(@PathVariable Long id) {
        DeliveryPartner partner = deliveryPartnerService.getDeliveryPartner(id);
        DeliveryPartnerResponse response = deliveryPartnerMapper.toResponse(partner);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{partnerId}/assign/{orderId}")
    @Operation(summary = "Assign delivery partner to order")
    public ResponseEntity<DeliveryPartnerResponse> assignToOrder(
            @PathVariable Long partnerId,
            @PathVariable Long orderId) {

        DeliveryPartner partner = deliveryPartnerService.assignToOrder(orderId, partnerId);
        DeliveryPartnerResponse response = deliveryPartnerMapper.toResponse(partner);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available/{city}")
    @Operation(summary = "Get available delivery partners by city")
    public ResponseEntity<List<DeliveryPartnerResponse>> getAvailablePartners(
            @PathVariable String city,
            @RequestParam(defaultValue = "CITY_BASED") String strategy) {

        List<DeliveryPartner> partners = deliveryPartnerService.findAvailablePartnersByCity(city, strategy);
        List<DeliveryPartnerResponse> response = partners.stream()
            .map(deliveryPartnerMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "Update delivery partner status")
    public ResponseEntity<DeliveryPartnerResponse> updateStatus(
            @PathVariable Long id,
            @PathVariable String status) {

        DeliveryPartner partner = deliveryPartnerService.updatePartnerStatus(id, status);
        DeliveryPartnerResponse response = deliveryPartnerMapper.toResponse(partner);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get delivery partners by status")
    public ResponseEntity<List<DeliveryPartnerResponse>> getPartnersByStatus(
            @PathVariable String status) {

        List<DeliveryPartner> partners = deliveryPartnerService.getPartnersByStatus(status);
        List<DeliveryPartnerResponse> response = partners.stream()
            .map(deliveryPartnerMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new delivery partner")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DeliveryPartnerResponse> createPartner(
            @RequestBody DeliveryPartner partner) {

        DeliveryPartner created = deliveryPartnerService.createPartner(partner);
        DeliveryPartnerResponse response = deliveryPartnerMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}