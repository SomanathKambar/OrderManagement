package com.example.ordermanagement.controller.dto.request;


import com.example.ordermanagement.domain.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update order status")
public class UpdateOrderStatusRequest {
    
    @NotNull(message = "Status is required")
    @Schema(description = "New order status", example = "ASSIGNED")
    private OrderStatus status;
    
    @Schema(description = "Additional notes for status update", example = "Assigned to delivery partner")
    private String notes;
}