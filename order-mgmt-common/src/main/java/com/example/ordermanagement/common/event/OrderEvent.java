package com.example.ordermanagement.common.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderCreatedEvent.class, name = "OrderCreatedEvent"),
        @JsonSubTypes.Type(value = OrderInitiatedEvent.class, name = "OrderInitiatedEvent"),
        @JsonSubTypes.Type(value = OrderPaidEvent.class, name = "OrderPaidEvent"),
        @JsonSubTypes.Type(value = OrderConfirmedEvent.class, name = "OrderConfirmedEvent"),
        @JsonSubTypes.Type(value = OrderPreparingEvent.class, name = "OrderPreparingEvent"),
        @JsonSubTypes.Type(value = OrderReadyEvent.class, name = "OrderReadyEvent"),
        @JsonSubTypes.Type(value = OrderPickedUpEvent.class, name = "OrderPickedUpEvent"),
        @JsonSubTypes.Type(value = OrderInTransitEvent.class, name = "OrderInTransitEvent"),
        @JsonSubTypes.Type(value = OrderCompletedEvent.class, name = "OrderCompletedEvent"),
        @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "OrderCancelledEvent"),
        @JsonSubTypes.Type(value = OrderFailedEvent.class, name = "OrderFailedEvent"),
        @JsonSubTypes.Type(value = OrderRefundedEvent.class, name = "OrderRefundedEvent"),
        @JsonSubTypes.Type(value = OrderAssignedEvent.class, name = "OrderAssignedEvent")
})
public sealed interface OrderEvent 
    permits OrderCreatedEvent, OrderInitiatedEvent, OrderPaidEvent, OrderConfirmedEvent, 
            OrderPreparingEvent, OrderReadyEvent, OrderPickedUpEvent, OrderInTransitEvent, 
            OrderCompletedEvent, OrderCancelledEvent, OrderFailedEvent, OrderRefundedEvent, 
            OrderAssignedEvent {
    
    String eventId();
    LocalDateTime occurredAt();
    Long orderId();
}
