package com.example.ordermanagement.modules.delivery.service;

import com.example.ordermanagement.modules.delivery.domain.DeliveryPartnerStatus;
import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.delivery.domain.model.DeliveryPartner;
import com.example.ordermanagement.modules.ordering.domain.model.Order;
import com.example.ordermanagement.modules.delivery.exception.DeliveryPartnerNotFoundException;
import com.example.ordermanagement.modules.ordering.exception.InvalidOrderStateException;
import com.example.ordermanagement.modules.delivery.domain.repository.DeliveryPartnerRepository;
import com.example.ordermanagement.modules.ordering.service.OrderService;
import com.example.ordermanagement.modules.delivery.service.strategy.AssignmentStrategyFactory;
import com.example.ordermanagement.modules.delivery.service.strategy.OrderAssignmentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final OrderService orderService;
    private final AssignmentStrategyFactory strategyFactory;

    @Cacheable(value = "delivery-partners", key = "#id")
    public DeliveryPartner getDeliveryPartner(Long id) {
        log.debug("Fetching delivery partner: {}", id);
        return deliveryPartnerRepository.findById(id)
                .orElseThrow(() -> new DeliveryPartnerNotFoundException(
                        "Delivery partner not found: " + id));
    }

    @CacheEvict(value = "delivery-partners", key = "#partnerId")
    public DeliveryPartner assignToOrder(Long orderId, Long partnerId) {
        log.info("Assigning delivery partner {} to order {}", partnerId, orderId);

        Order order = orderService.getOrder(orderId);
        DeliveryPartner partner = getDeliveryPartner(partnerId);

        if (!order.canBeAssigned()) {
            throw new InvalidOrderStateException(
                    "Order " + orderId + " cannot be assigned in current state: " + order.getStatus());
        }

        if (!partner.canAcceptOrder()) {
            throw new InvalidOrderStateException(
                    "Delivery partner " + partnerId + " cannot accept more orders. Current load: " +
                            partner.getAssignedOrderCount());
        }

        order.assignToDeliveryPartner(partner.getId());
        partner.assignOrder();

        deliveryPartnerRepository.save(partner);
        orderService.updateOrderStatus(orderId, OrderStatus.ASSIGNED);

        log.info("Successfully assigned partner {} to order {}", partnerId, orderId);
        return partner;
    }

    public List<DeliveryPartner> findAvailablePartnersByCity(String city, String strategyName) {
        log.info("Finding available partners in city: {} using strategy: {}", city, strategyName);

        List<DeliveryPartner> availablePartners =
                deliveryPartnerRepository.findAvailableByCity(city);

        if (availablePartners.isEmpty()) {
            log.warn("No available partners found in city: {}", city);
            return availablePartners;
        }

        return availablePartners;
    }

    public DeliveryPartner findOptimalPartner(Order order, String strategyName) {
        String city = order.getRestaurantAddress().getCity();
        List<DeliveryPartner> availablePartners = findAvailablePartnersByCity(city, strategyName);

        if (availablePartners.isEmpty()) {
            log.warn("No available partners found for order: {}", order.getId());
            return null;
        }

        OrderAssignmentStrategy strategy = strategyFactory.getStrategy(strategyName);
        return strategy.selectDeliveryPartner(order, availablePartners);
    }

    public Page<DeliveryPartner> getAllPartners(Pageable pageable) {
        log.debug("Getting all delivery partners with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return deliveryPartnerRepository.findAll(pageable);
    }

    public List<DeliveryPartner> getPartnersByStatus(String status) {
        log.debug("Getting delivery partners by status: {}", status);
        try {
            DeliveryPartnerStatus partnerStatus = DeliveryPartnerStatus.valueOf(status);
            return deliveryPartnerRepository.findByStatus(partnerStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    public DeliveryPartner createPartner(DeliveryPartner partner) {
        log.info("Creating new delivery partner: {}", partner.getName());

        // Set default values if not provided
        if (partner.getStatus() == null) {
            partner.setStatus(DeliveryPartnerStatus.AVAILABLE);
        }
        if (partner.getAssignedOrderCount() == null) {
            partner.setAssignedOrderCount(0);
        }
        if (partner.getIsActive() == null) {
            partner.setIsActive(true);
        }

        return deliveryPartnerRepository.save(partner);
    }

    public DeliveryPartner updatePartnerStatus(Long partnerId, String status) {
        DeliveryPartner partner = getDeliveryPartner(partnerId);

        try {
            partner.setStatus(DeliveryPartnerStatus.valueOf(status));
            DeliveryPartner updated = deliveryPartnerRepository.save(partner);

            log.info("Updated partner {} status to {}", partnerId, status);
            return updated;

        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStateException("Invalid status: " + status);
        }
    }
}