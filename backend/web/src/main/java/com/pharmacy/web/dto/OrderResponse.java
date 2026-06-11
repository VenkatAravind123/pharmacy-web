package com.pharmacy.web.dto;




import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(

        Long orderId,

        BigDecimal totalAmount,

        String status,

        LocalDateTime createdAt
) {
}