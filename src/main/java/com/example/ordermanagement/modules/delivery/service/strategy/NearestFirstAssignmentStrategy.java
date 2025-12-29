package com.example.ordermanagement.modules.delivery.service.strategy;


import com.example.ordermanagement.common.domain.Address;
import com.example.ordermanagement.modules.delivery.domain.model.DeliveryPartner;
import com.example.ordermanagement.modules.ordering.domain.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class NearestFirstAssignmentStrategy implements OrderAssignmentStrategy {
    
    @Override
    public DeliveryPartner selectDeliveryPartner(Order order, List<DeliveryPartner> partners) {
        log.info("Using NearestFirstAssignmentStrategy for order: {}", order.getId());
        
        if (partners.isEmpty()) {
            log.warn("No available delivery partners found");
            return null;
        }
        
        Address restaurantAddress = order.getRestaurantAddress();
        
        return partners.stream()
            .filter(DeliveryPartner::canAcceptOrder)
            .min(Comparator.comparingDouble(partner -> 
                calculateDistance(
                    partner.getCurrentLocation(),
                    restaurantAddress
                )
            ))
            .orElse(null);
    }
    
    @Override
    public String getStrategyName() {
        return "NEAREST_FIRST";
    }
    
    /**
     * Calculate distance between two locations using Haversine formula
     */
    private double calculateDistance(Address location1, Address location2) {
        if (location1 == null || location2 == null || 
            location1.getLatitude() == null || location1.getLongitude() == null ||
            location2.getLatitude() == null || location2.getLongitude() == null) {
            return Double.MAX_VALUE;
        }
        
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());
        
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        
        double a = Math.pow(Math.sin(dlat / 2), 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.pow(Math.sin(dlon / 2), 2);
        
        double c = 2 * Math.asin(Math.sqrt(a));
        
        // Radius of earth in kilometers
        double r = 6371;
        
        return c * r;
    }
}