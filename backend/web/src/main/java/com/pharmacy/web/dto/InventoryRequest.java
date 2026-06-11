package com.pharmacy.web.dto;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryRequest(

        @NotNull
        Long medicineId,

        @Min(value = 0)
        Integer quantity
) {
}