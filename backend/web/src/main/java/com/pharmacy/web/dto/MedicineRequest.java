package com.pharmacy.web.dto;



import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MedicineRequest(

        @NotBlank
        String name,

        String description,

        String dosage,

        String manufacturer,

        @Positive
        BigDecimal price,

        Boolean requiresPrescription,

        @NotNull
        Long categoryId

) {
}