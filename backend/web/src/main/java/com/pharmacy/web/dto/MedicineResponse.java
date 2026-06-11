package com.pharmacy.web.dto;


import java.math.BigDecimal;

public record MedicineResponse(

        Long id,

        String name,

        String description,

        String dosage,

        String manufacturer,

        BigDecimal price,

        Boolean requiresPrescription,

        Long categoryId,

        String categoryName

) {
}