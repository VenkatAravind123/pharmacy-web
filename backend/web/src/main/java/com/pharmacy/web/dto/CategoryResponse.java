package com.pharmacy.web.dto;

public record CategoryResponse(
        Long id,
        String name,
        String description
) {
}