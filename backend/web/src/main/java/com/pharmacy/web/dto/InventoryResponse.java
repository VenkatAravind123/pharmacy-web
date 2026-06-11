package com.pharmacy.web.dto;



public record InventoryResponse(

        Long inventoryId,

        Long medicineId,

        String medicineName,

        Integer quantity
) {
}
