package com.pharmacy.web.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long medicineId,
        String medicineName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
}
