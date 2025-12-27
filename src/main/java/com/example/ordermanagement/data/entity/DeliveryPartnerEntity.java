package com.example.ordermanagement.data.entity;


import com.example.ordermanagement.domain.enums.DeliveryPartnerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_partners")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeliveryPartnerStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "current_city", nullable = false, length = 50)),
            @AttributeOverride(name = "streetAddress", column = @Column(name = "current_street", length = 200)),
            @AttributeOverride(name = "state", column = @Column(name = "current_state", length = 50)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "current_postal_code", length = 20)),
            @AttributeOverride(name = "country", column = @Column(name = "current_country", length = 50)),
            @AttributeOverride(name = "latitude", column = @Column(name = "current_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "current_longitude"))
    })
    private AddressEmbeddable currentLocation;

    @Column(name = "vehicle_type", length = 20)
    private String vehicleType;

    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber;

    @Column(name = "assigned_order_count", nullable = false)
    @Builder.Default
    private Integer assignedOrderCount = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}