package com.pharmacy.web.service;

import com.pharmacy.web.dto.OrderItemRequest;
import com.pharmacy.web.dto.OrderItemResponse;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryService inventoryService;
    private final PrescriptionRepository prescriptionRepository;

    public OrderResponse placeOrder(OrderRequest request) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING) // Always starts as PENDING waiting for Payment!
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        order = orderRepository.save(order);
        BigDecimal totalAmount = BigDecimal.ZERO;
        Medicine medicine;

        for (OrderItemRequest itemRequest : request.items()) {
            medicine = medicineRepository.findById(itemRequest.medicineId()).orElseThrow();
            if (!inventoryService.hasEnoughStock(medicine.getId(), itemRequest.quantity())) {
                throw new OutOfStockException(medicine.getName() + " is out of stock");
            }

            BigDecimal subTotal = medicine.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalAmount = totalAmount.add(subTotal);

            OrderItem orderItem = OrderItem.builder().order(order).medicine(medicine).quantity(itemRequest.quantity()).unitPrice(medicine.getPrice()).subTotal(subTotal).build();
            orderItemRepository.save(orderItem);
            inventoryService.reduceStock(medicine.getId(), itemRequest.quantity());
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

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse updateOrderStatus(
    		Long orderId,
    		String status) {

    	
    		Order order = orderRepository.findById(orderId)
    		        .orElseThrow(() ->
    		                new ResourceNotFoundException(
    		                        "Order not found"));

    		OrderStatus newStatus;

    		try {
    		    newStatus = OrderStatus.valueOf (
    		            status.toUpperCase());
    		} catch (Exception ex) {
    		    throw new RuntimeException(
    		            "Invalid Order Status");
    		}

    		order.setStatus(newStatus);

    		if (newStatus == OrderStatus.REJECTED
    		        || newStatus == OrderStatus.CANCELLED) {

    		    List<OrderItem> items =
    		            orderItemRepository.findByOrderId(orderId);

    		    for (OrderItem item : items) {

    		        inventoryService.increaseStock(
    		                item.getMedicine().getId(),
    		                item.getQuantity());
    		    }
    		}

    		order = orderRepository.save(order);

    		return mapToResponse(order);
    		

    		}


    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = orderItemRepository.findByOrderId(order.getId())
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getMedicine().getId(),
                        item.getMedicine().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubTotal()
                ))
                .toList();
                
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findByOrderId(order.getId());
        Long prescriptionId = prescriptionOpt.map(Prescription::getId).orElse(null);
        String prescriptionFileName = prescriptionOpt.map(Prescription::getFileName).orElse(null);

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getPaymentStatus().name(),
                order.getCreatedAt(),
                itemResponses,
                prescriptionId,
                prescriptionFileName
        );
    }
    public OrderResponse handlePayment(Long orderId, String paymentStatusStr) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // 1. IF THE TIMER RAN OUT (FAILED)
        if ("FAILED".equals(paymentStatusStr)) {
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);

            // Put the medicines back in the inventory since they didn't pay!
            List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
            for (OrderItem item : items) {
                inventoryService.increaseStock(item.getMedicine().getId(), item.getQuantity());
            }
            order = orderRepository.save(order);
            return mapToResponse(order);
        }

        // 2. IF THEY CLICKED "I HAVE COMPLETED PAYMENT" (SUCCESS)
        order.setPaymentStatus(PaymentStatus.SUCCESS);
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        boolean requiresPrescription = items.stream().anyMatch(item -> item.getMedicine().getRequiresPrescription());

        if (requiresPrescription) {
            order.setStatus(OrderStatus.PRESCRIPTION_REVIEW); // Send to pharmacist
        } else {
            order.setStatus(OrderStatus.APPROVED); // Bypass pharmacist
        }

        order = orderRepository.save(order);
        return mapToResponse(order);
    }
    
    public OrderResponse packOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Guard Clause: Only APPROVED orders can be packed
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalStateException("Order must be APPROVED before it can be packed. Current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.PACKED);
        order = orderRepository.save(order);
        return mapToResponse(order);
    }

    public OrderResponse deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Guard Clause: Only PACKED orders can be marked as delivered
        if (order.getStatus() != OrderStatus.PACKED) {
            throw new IllegalStateException("Order must be PACKED before it can be delivered. Current status: " + order.getStatus());
        }

        // Since it's delivered, ensure payment is settled (if it wasn't already COD/Online)
        order.setStatus(OrderStatus.DELIVERED);
        order = orderRepository.save(order);
        return mapToResponse(order);
    }
}