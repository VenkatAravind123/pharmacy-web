package com.pharmacy.web.controller;



import com.pharmacy.web.dto.OrderResponse;
import com.pharmacy.web.dto.UpdateOrderStatusRequest;
import com.pharmacy.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderService orderService;

    @PutMapping("/orders/{id}/status")
    public OrderResponse updateOrderStatus(

            @PathVariable Long id,

            @RequestBody
            UpdateOrderStatusRequest request) {

        return orderService.updateOrderStatus(
                id,
                request.status());
    }
}
