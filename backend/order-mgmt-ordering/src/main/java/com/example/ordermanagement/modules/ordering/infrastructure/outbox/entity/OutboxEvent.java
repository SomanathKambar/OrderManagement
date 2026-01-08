package com.example.ordermanagement.modules.ordering.infrastructure.outbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String aggregateType; // e.g., "ORDER"

    @Column(nullable = false)
    private String aggregateId;   // e.g., "101"

    @Column(nullable = false)
    private String eventType;     // e.g., "OrderCreatedEvent"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;       // JSON content

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Builder.Default
    private Integer retryCount = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;
    
    private String topic;

    public enum OutboxStatus {
        PENDING,
        PUBLISHED,
        FAILED
    }
}
