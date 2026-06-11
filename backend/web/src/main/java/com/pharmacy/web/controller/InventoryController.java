package com.pharmacy.web.controller;


import com.pharmacy.web.dto.*;
import com.pharmacy.web.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public InventoryResponse addStock(
            @Valid
            @RequestBody InventoryRequest request) {

        return inventoryService.addStock(request);
    }

    @GetMapping("/{medicineId}")
    public InventoryResponse getStock(
            @PathVariable Long medicineId) {

        return inventoryService.getStock(medicineId);
    }

    @PutMapping("/{medicineId}")
    public InventoryResponse updateStock(
            @PathVariable Long medicineId,
            @Valid
            @RequestBody InventoryUpdateRequest request) {

        return inventoryService.updateStock(
                medicineId,
                request.quantity()
        );
    }
    @GetMapping
    public List<InventoryResponse> getAllInventory() {

        return inventoryService.getAllInventory();
    }
    @GetMapping("/low-stock")
    public List<InventoryResponse> lowStockMedicines() {

        return inventoryService.getLowStockMedicines();
    }
}
