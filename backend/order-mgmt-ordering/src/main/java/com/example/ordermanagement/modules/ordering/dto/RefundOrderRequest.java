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
@Schema(description = "Request to refund an order")
public class RefundOrderRequest {

    @NotNull(message = "Refund reason is required")
    @Schema(description = "Reason for the refund", example = "Order was cancelled by customer")
    private String reason;
}
