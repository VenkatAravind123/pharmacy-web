package com.pharmacy.web.controller;


import com.pharmacy.web.dto.OrderRequest;
import com.pharmacy.web.dto.OrderResponse;
import com.pharmacy.web.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse placeOrder(
            @Valid
            @RequestBody OrderRequest request) {

        return orderService.placeOrder(request);
    }

    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {

        return orderService.getMyOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(
            @PathVariable Long orderId) {

        return orderService.getOrderById(orderId);
    }

    @PutMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(
            @PathVariable Long orderId) {

        return orderService.cancelOrder(orderId);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{orderId}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }
    @PutMapping("/{orderId}/payment")
    public OrderResponse handlePayment(
            @PathVariable Long orderId,
            @RequestBody java.util.Map<String, String> payload) {

        // This grabs the { status: 'FAILED' } or { status: 'COMPLETED' } from the React frontend!
        return orderService.handlePayment(orderId, payload.get("status"));
    }
    
    @PutMapping("/{id}/pack")
    public ResponseEntity<OrderResponse> packOrder(@PathVariable Long id) {
        OrderResponse response = orderService.packOrder(id);
        return ResponseEntity.ok(response);
    }

    // 2. Endpoint to mark a PACKED order as DELIVERED
    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable Long id) {
        OrderResponse response = orderService.deliverOrder(id);
        return ResponseEntity.ok(response);
}
}