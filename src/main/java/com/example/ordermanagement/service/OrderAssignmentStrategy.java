package com.example.ordermanagement.service;


import com.example.ordermanagement.domain.model.DeliveryPartner;
import com.example.ordermanagement.domain.model.Order;

import java.util.List;

public interface OrderAssignmentStrategy {
    DeliveryPartner selectDeliveryPartner(Order order, List<DeliveryPartner> availablePartners);
    String getStrategyName();
}