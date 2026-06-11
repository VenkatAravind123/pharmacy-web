package com.pharmacy.web.controller;


import com.pharmacy.web.dto.OrderRequest;
import com.pharmacy.web.dto.OrderResponse;
import com.pharmacy.web.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}