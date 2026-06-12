package com.pharmacy.web.dto;



import jakarta.validation.constraints.Min;

public record InventoryUpdateRequest(
        @Min(value = 0)
        Integer quantity
) {
}
