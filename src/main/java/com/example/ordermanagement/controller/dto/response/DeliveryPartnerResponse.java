package com.example.ordermanagement.controller.dto.response;

import com.example.ordermanagement.domain.enums.DeliveryPartnerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery partner response")
public class DeliveryPartnerResponse {
    
    @Schema(description = "Delivery partner ID", example = "123")
    private Long id;
    
    @Schema(description = "Delivery partner name", example = "Rahul Sharma")
    private String name;
    
    @Schema(description = "Phone number", example = "+919876543210")
    private String phoneNumber;
    
    @Schema(description = "Email", example = "rahul@swiggy.com")
    private String email;
    
    @Schema(description = "Current status", example = "AVAILABLE")
    private DeliveryPartnerStatus status;
    
    @Schema(description = "Current city", example = "Bangalore")
    private String currentCity;
    
    @Schema(description = "Vehicle type", example = "BIKE")
    private String vehicleType;
    
    @Schema(description = "Vehicle number", example = "KA01AB1234")
    private String vehicleNumber;
    
    @Schema(description = "Number of assigned orders", example = "1")
    private Integer assignedOrderCount;
    
    @Schema(description = "Is partner active", example = "true")
    private Boolean isActive;
}