package com.example.ordermanagement.modules.ordering.dto;


import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
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
    
    @NotNull(message = "Target state is required")
    @Schema(description = "Target state for transition", example = "PREPARING")
    private OrderStatus targetState;
    
    @Schema(description = "Reason for state transition", example = "Restaurant accepted order")
    private String reason;
}