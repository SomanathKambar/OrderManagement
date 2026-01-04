package com.example.ordermanagement.modules.ordering.api;

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
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        log.info("Creating order for customer: {}", request.getCustomerId());

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
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        Order order = orderService.updateOrderStatus(id, request.getStatus());
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel order")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {

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