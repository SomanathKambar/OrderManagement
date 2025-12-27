package com.example.ordermanagement.service;

import com.example.ordermanagement.domain.model.DeliveryPartner;
import com.example.ordermanagement.domain.model.Order;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component
public class CityBasedAssignmentStrategy implements OrderAssignmentStrategy {
    
    @Override
    public DeliveryPartner selectDeliveryPartner(Order order, List<DeliveryPartner> partners) {
        return partners.stream()
            .filter(p -> p.getCurrentLocation().getCity().equals(
                order.getRestaurantAddress().getCity()))
            .filter(DeliveryPartner::canAcceptOrder)
            .sorted(Comparator.comparing(dp -> 
                dp.getAssignedOrderCount() != null ? dp.getAssignedOrderCount() : 0))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public String getStrategyName() {
        return "CITY_BASED";
    }
}