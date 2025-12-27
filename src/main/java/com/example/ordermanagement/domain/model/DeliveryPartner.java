package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.enums.DeliveryPartnerStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartner {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private DeliveryPartnerStatus status;
    private Address currentLocation;
    private String vehicleType;
    private String vehicleNumber;
    private Integer assignedOrderCount;
    private Boolean isActive;

    public boolean isAvailable() {
        return isActive && status == DeliveryPartnerStatus.AVAILABLE;
    }

    public boolean canAcceptOrder() {
        return isAvailable() && (assignedOrderCount == null || assignedOrderCount < 3);
    }

    public void assignOrder() {
        if (!canAcceptOrder()) {
            throw new IllegalStateException("Delivery partner cannot accept more orders");
        }
        this.assignedOrderCount = (assignedOrderCount == null ? 0 : assignedOrderCount) + 1;
        if (this.assignedOrderCount >= 3) {
            this.status = DeliveryPartnerStatus.BUSY;
        }
    }

    public void releaseOrder() {
        if (assignedOrderCount != null && assignedOrderCount > 0) {
            this.assignedOrderCount--;
            if (this.assignedOrderCount < 3) {
                this.status = DeliveryPartnerStatus.AVAILABLE;
            }
        }
    }
}