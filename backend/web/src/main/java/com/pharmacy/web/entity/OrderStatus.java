package com.pharmacy.web.entity;



public enum OrderStatus {

    PENDING,
    CONFIRMED, // Keeping this so old database test records don't crash the app
    
    VALIDATED, // Keeping this too for old test records!
    
    APPROVED,
    
    REJECTED,

    PACKED,

    DELIVERED,

    CANCELLED,
    
    REJECTED
}