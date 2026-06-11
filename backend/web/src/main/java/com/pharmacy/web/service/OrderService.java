package com.pharmacy.web.service;

import com.pharmacy.web.dto.OrderItemRequest;
import com.pharmacy.web.dto.OrderRequest;
import com.pharmacy.web.dto.OrderResponse;
import com.pharmacy.web.entity.*;
import com.pharmacy.web.exception.OutOfStockException;
import com.pharmacy.web.exception.ResourceNotFoundException;
import com.pharmacy.web.Repository.*;
import com.pharmacy.web.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryService inventoryService;

    public OrderResponse placeOrder(OrderRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        order = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {

            Medicine medicine = medicineRepository
                    .findById(itemRequest.medicineId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Medicine not found with id : "
                                            + itemRequest.medicineId()));

            if (!inventoryService.hasEnoughStock(
                    medicine.getId(),
                    itemRequest.quantity())) {

                throw new OutOfStockException(
                        medicine.getName() + " is out of stock");
            }

            BigDecimal subTotal = medicine.getPrice()
                    .multiply(
                            BigDecimal.valueOf(
                                    itemRequest.quantity()));

            totalAmount = totalAmount.add(subTotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .medicine(medicine)
                    .quantity(itemRequest.quantity())
                    .unitPrice(medicine.getPrice())
                    .subTotal(subTotal)
                    .build();

            orderItemRepository.save(orderItem);

            inventoryService.reduceStock(
                    medicine.getId(),
                    itemRequest.quantity());
        }

        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);

        return mapToResponse(order);
    }

    public List<OrderResponse> getMyOrders() {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long orderId) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not authorized to view this order");
        }

        return mapToResponse(order);
    }

    public OrderResponse cancelOrder(Long orderId) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not authorized to cancel this order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException(
                    "Order already cancelled");
        }

        List<OrderItem> items =
                orderItemRepository.findByOrderId(orderId);

        for (OrderItem item : items) {

            inventoryService.increaseStock(
                    item.getMedicine().getId(),
                    item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}