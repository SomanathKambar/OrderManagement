package com.example.ordermanagement.modules.ordering.api;

import com.example.ordermanagement.modules.ordering.dto.RefundOrderRequest;
import com.example.ordermanagement.modules.ordering.dto.AssignOrderRequest;
import com.example.ordermanagement.modules.ordering.dto.CreateOrderRequest;
import com.example.ordermanagement.modules.ordering.dto.OrderResponse;
import com.example.ordermanagement.modules.ordering.dto.UpdateOrderStatusRequest;
import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.model.Order;
import com.example.ordermanagement.modules.ordering.service.OrderService;
import com.example.ordermanagement.modules.ordering.mapper.OrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order management endpoints")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    @Operation(summary = "Create a new order")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = "Idempotency-Key") String idempotencyKey) {

        log.info("Creating order for customer: {} with key: {}", request.getCustomerId(), idempotencyKey);

        Order order = orderService.createOrder(
                request.getCustomerId(),
                request.getCustomerName(),
                request.getRestaurantId(),
                request.getRestaurantName(),
                request.getOrderType().name(),
                request.getDeliveryAddress(),
                request.getRestaurantAddress(),
                request.getItems(),
                request.getSpecialInstructions()
        );

        OrderResponse response = orderMapper.toResponse(order);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/v1/orders/" + order.getId())
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            @RequestHeader(value = "Idempotency-Key") String idempotencyKey) {
            
        log.info("Updating order {} status to {} with key: {}", id, request.getTargetState(), idempotencyKey);

        Order order = orderService.updateOrderStatus(id, request.getTargetState(), request.getReason());
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign order to delivery partner")
    public ResponseEntity<OrderResponse> assignOrder(
            @PathVariable Long id,
            @Valid @RequestBody AssignOrderRequest request,
            @RequestHeader(value = "Idempotency-Key") String idempotencyKey) {

        log.info("Assigning order {} to partner {} with key: {}", id, request.getDeliveryPartnerId(), idempotencyKey);

        Order order = orderService.assignOrder(id, request.getDeliveryPartnerId());
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund an order")
    public ResponseEntity<OrderResponse> refundOrder(
            @PathVariable Long id,
            @Valid @RequestBody RefundOrderRequest request,
            @RequestHeader(value = "Idempotency-Key") String idempotencyKey) {

        log.info("Refunding order {} with key: {}", id, idempotencyKey);

        Order order = orderService.refundOrder(id, request.getReason());
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel order")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            @RequestHeader(value = "Idempotency-Key") String idempotencyKey) {
            
        log.info("Cancelling order {} with key: {}", id, idempotencyKey);

        Order order = orderService.cancelOrder(id, reason != null ? reason : "Customer request");
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Search orders with filters")
    public ResponseEntity<Page<OrderResponse>> searchOrders(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String restaurantId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String city,
            Pageable pageable) {

        Page<Order> orders = orderService.searchOrders(customerId, restaurantId, status, city, pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);
        return ResponseEntity.ok(response);
    }
}