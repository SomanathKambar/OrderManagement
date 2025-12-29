package com.example.ordermanagement.modules.delivery.service.strategy;


import com.example.ordermanagement.modules.delivery.domain.model.DeliveryPartner;
import com.example.ordermanagement.modules.ordering.domain.model.Order;

import java.util.List;

public interface OrderAssignmentStrategy {
    DeliveryPartner selectDeliveryPartner(Order order, List<DeliveryPartner> availablePartners);
    String getStrategyName();
}