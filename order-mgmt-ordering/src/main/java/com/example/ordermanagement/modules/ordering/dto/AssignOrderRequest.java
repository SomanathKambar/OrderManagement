package com.example.ordermanagement.modules.ordering.dto;

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
@Schema(description = "Request to assign a delivery partner to an order")
public class AssignOrderRequest {

    @NotNull(message = "Delivery partner ID is required")
    @Schema(description = "ID of the delivery partner", example = "101")
    private Long deliveryPartnerId;
}
