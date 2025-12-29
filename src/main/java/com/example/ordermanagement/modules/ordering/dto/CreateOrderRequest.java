package com.example.ordermanagement.modules.ordering.dto;


import com.example.ordermanagement.modules.ordering.domain.OrderType;
import com.example.ordermanagement.common.domain.Address;
import com.example.ordermanagement.modules.ordering.domain.model.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new order")
public class CreateOrderRequest {
    
    @NotBlank(message = "Customer ID is required")
    @Schema(description = "Unique identifier of the customer", example = "cust_12345")
    private String customerId;
    
    @NotBlank(message = "Customer name is required")
    @Schema(description = "Name of the customer", example = "John Doe")
    private String customerName;
    
    @NotBlank(message = "Restaurant ID is required")
    @Schema(description = "Unique identifier of the restaurant", example = "rest_67890")
    private String restaurantId;
    
    @NotBlank(message = "Restaurant name is required")
    @Schema(description = "Name of the restaurant", example = "Pizza Hut")
    private String restaurantName;
    
    @NotNull(message = "Order type is required")
    @Schema(description = "Type of the order", example = "FOOD")
    private OrderType orderType;
    
    @NotNull(message = "Delivery address is required")
    @Valid
    @Schema(description = "Delivery address")
    private Address deliveryAddress;
    
    @NotNull(message = "Restaurant address is required")
    @Valid
    @Schema(description = "Restaurant address")
    private Address restaurantAddress;
    
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    @Schema(description = "List of order items")
    private List<OrderItem> items;
    
    @Schema(description = "Special instructions for delivery", example = "Leave at door")
    private String specialInstructions;
}