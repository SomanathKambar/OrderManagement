package com.example.ordermanagement;

import com.example.ordermanagement.modules.ordering.dto.CreateOrderRequest;
import com.example.ordermanagement.modules.ordering.dto.UpdateOrderStatusRequest;
import com.example.ordermanagement.modules.ordering.domain.OrderStatus;
import com.example.ordermanagement.modules.ordering.domain.OrderType;
import com.example.ordermanagement.common.domain.Address;
import com.example.ordermanagement.modules.ordering.domain.model.Order;
import com.example.ordermanagement.modules.ordering.domain.model.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerIntegrationTest {

}
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class OrderControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Order createTestOrder(String customerId, String restaurantId, OrderType orderType, OrderStatus status) throws Exception {
//        CreateOrderRequest request = CreateOrderRequest.builder()
//                .customerId(customerId)
//                .customerName("Test Customer")
//                .restaurantId(restaurantId)
//                .restaurantName("Test Restaurant")
//                .orderType(orderType)
//                .deliveryAddress(Address.builder().city("Bangalore").streetAddress("123 Main St").build())
//                .restaurantAddress(Address.builder().city("Bangalore").streetAddress("456 Restaurant Ave").build())
//                .items(Collections.singletonList(OrderItem.builder()
//                        .itemId("ITEM001")
//                        .itemName("Test Item")
//                        .quantity(1)
//                        .unitPrice(new BigDecimal("10.00"))
//                        .build()))
//                .specialInstructions("Test Instructions")
//                .build();
//
//        MvcResult result = mockMvc.perform(post("/api/v1/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        Order createdOrder = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);
//        if (status != OrderStatus.CREATED) {
//            UpdateOrderStatusRequest updateStatusRequest = new UpdateOrderStatusRequest(status);
//            mockMvc.perform(put("/api/v1/orders/" + createdOrder.getId() + "/status")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(updateStatusRequest)))
//                    .andExpect(status().isOk());
//            return objectMapper.readValue(mockMvc.perform(get("/api/v1/orders/" + createdOrder.getId()))
//                    .andReturn().getResponse().getContentAsString(), Order.class);
//        }
//        return createdOrder;
//    }
//
//
//    @Test
//    void testCreateOrder() throws Exception {
//        CreateOrderRequest request = CreateOrderRequest.builder()
//                .customerId("CUST001")
//                .customerName("John Doe")
//                .restaurantId("REST001")
//                .restaurantName("Pizza Hut")
//                .orderType(OrderType.FOOD)
//                .deliveryAddress(Address.builder().city("Bangalore").build())
//                .restaurantAddress(Address.builder().city("Bangalore").build())
//                .items(Collections.singletonList(OrderItem.builder()
//                        .itemId("ITEM001")
//                        .itemName("Margherita Pizza")
//                        .quantity(1)
//                        .unitPrice(new BigDecimal("10.00"))
//                        .build()))
//                .specialInstructions("Leave at door")
//                .build();
//
//        mockMvc.perform(post("/api/v1/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").isNotEmpty());
//    }
//
//    @Test
//    void testGetOrder() throws Exception {
//        Order createdOrder = createTestOrder("CUST002", "REST002", OrderType.FOOD, OrderStatus.CREATED);
//
//        mockMvc.perform(get("/api/v1/orders/" + createdOrder.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.customerId").value("CUST002"));
//    }
//
//    @Test
//    void testUpdateOrderStatus() throws Exception {
//        Order createdOrder = createTestOrder("CUST003", "REST003", OrderType.FOOD, OrderStatus.CREATED);
//
//        UpdateOrderStatusRequest updateStatusRequest = new UpdateOrderStatusRequest(OrderStatus.ASSIGNED);
//
//        mockMvc.perform(put("/api/v1/orders/" + createdOrder.getId() + "/status")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateStatusRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("ASSIGNED"));
//    }
//
//    @Test
//    void testCancelOrder() throws Exception {
//        Order createdOrder = createTestOrder("CUST004", "REST004", OrderType.FOOD, OrderStatus.CREATED);
//
//        mockMvc.perform(delete("/api/v1/orders/" + createdOrder.getId())
//                        .param("reason", "Customer changed mind"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("CANCELLED"))
//                .andExpect(jsonPath("$.cancellationReason").value("Customer changed mind"));
//    }
//}
