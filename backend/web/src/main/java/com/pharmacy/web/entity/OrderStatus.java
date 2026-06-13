package com.pharmacy.web.entity;

public enum OrderStatus {
    PENDING,
    PRESCRIPTION_REVIEW,
    APPROVED,
    PACKED,
    DELIVERED,
    CANCELLED,
    REJECTED
}