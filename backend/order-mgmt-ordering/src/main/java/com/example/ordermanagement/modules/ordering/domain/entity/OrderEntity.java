package com.example.ordermanagement.modules.ordering.domain.entity;

import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.OrderType;
import com.example.ordermanagement.modules.ordering.domain.PaymentStatus;
import com.example.ordermanagement.common.domain.AddressEmbeddable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "restaurant_id", nullable = false, length = 50)
    private String restaurantId;

    @Column(name = "restaurant_name", nullable = false, length = 100)
    private String restaurantName;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 20)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "delivery_city", nullable = false, length = 50)),
            @AttributeOverride(name = "streetAddress", column = @Column(name = "delivery_street", length = 200)),
            @AttributeOverride(name = "state", column = @Column(name = "delivery_state", length = 50)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code", length = 20)),
            @AttributeOverride(name = "country", column = @Column(name = "delivery_country", length = 50)),
            @AttributeOverride(name = "latitude", column = @Column(name = "delivery_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "delivery_longitude"))
    })
    private AddressEmbeddable deliveryAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "restaurant_city", nullable = false, length = 50)),
            @AttributeOverride(name = "streetAddress", column = @Column(name = "restaurant_street", length = 200)),
            @AttributeOverride(name = "state", column = @Column(name = "restaurant_state", length = 50)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "restaurant_postal_code", length = 20)),
            @AttributeOverride(name = "country", column = @Column(name = "restaurant_country", length = 50)),
            @AttributeOverride(name = "latitude", column = @Column(name = "restaurant_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "restaurant_longitude"))
    })
    private AddressEmbeddable restaurantAddress;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order", orphanRemoval = true)
    @Builder.Default
    private List<OrderItemEntity> items = new ArrayList<>();

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
        for (OrderItemEntity item : items) {
            item.setOrder(this);
        }
    }

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "delivery_charge", precision = 10, scale = 2)
    private BigDecimal deliveryCharge;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "grand_total", precision = 10, scale = 2)
    private BigDecimal grandTotal;

    @Column(name = "delivery_partner_id")
    private Long deliveryPartnerId;

    @Column(name = "estimated_delivery_minutes")
    private Integer estimatedDeliveryMinutes;

    @Column(name = "actual_delivery_minutes")
    private Integer actualDeliveryMinutes;

    @Column(name = "special_instructions", length = 500)
    private String specialInstructions;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "picked_at")
    private LocalDateTime pickedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
}