package com.pharmacy.web.dto;


public record PrescriptionResponse(

        Long id,

        String fileName,

        String status
) {
}