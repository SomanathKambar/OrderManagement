package com.example.ordermanagement.modules.delivery.service.strategy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AssignmentStrategyFactory {
    
    private final CityBasedAssignmentStrategy cityBasedStrategy;
    private final NearestFirstAssignmentStrategy nearestFirstStrategy;
    
    private Map<String, OrderAssignmentStrategy> strategies = new HashMap<>();
    
    @PostConstruct
    public void init() {
        strategies.put(cityBasedStrategy.getStrategyName(), cityBasedStrategy);
        strategies.put(nearestFirstStrategy.getStrategyName(), nearestFirstStrategy);
    }
    
    public OrderAssignmentStrategy getStrategy(String name) {
        if (name == null || name.trim().isEmpty()) {
            return strategies.get("CITY_BASED"); // Default strategy
        }
        
        OrderAssignmentStrategy strategy = strategies.get(name.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown strategy: " + name);
        }
        return strategy;
    }
    
    public List<String> getAvailableStrategies() {
        return List.copyOf(strategies.keySet());
    }
}