package com.pharmacy.web.dto;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(

        @NotNull
        Long medicineId,

        @Min(1)
        Integer quantity
) {
}
