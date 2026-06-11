package com.pharmacy.web.service;



import com.pharmacy.web.dto.*;
import com.pharmacy.web.entity.*;
import com.pharmacy.web.exception.OutOfStockException;
import com.pharmacy.web.exception.ResourceNotFoundException;
import com.pharmacy.web.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MedicineRepository medicineRepository;

    public InventoryResponse addStock(
            InventoryRequest request) {
    	
    	if (request.quantity() <= 0) {
    	    throw new IllegalArgumentException(
    	            "Quantity must be greater than zero");
    	}

        Medicine medicine =
                medicineRepository.findById(
                        request.medicineId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medicine not found"));

        Inventory inventory =
                inventoryRepository
                        .findByMedicineId(
                                medicine.getId())
                        .orElse(null);

        if(inventory == null) {

            inventory = Inventory.builder()
                    .medicine(medicine)
                    .quantity(request.quantity())
                    .lastUpdated(LocalDateTime.now())
                    .build();

        } else {

            inventory.setQuantity(
                    inventory.getQuantity()
                            + request.quantity());

            inventory.setLastUpdated(
                    LocalDateTime.now());
        }

        inventory = inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    public InventoryResponse getStock(
            Long medicineId) {

        Inventory inventory =
                inventoryRepository
                        .findByMedicineId(medicineId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found"));

        return mapToResponse(inventory);
    }

    public InventoryResponse updateStock(
            Long medicineId,
            Integer quantity) {

        Inventory inventory =
                inventoryRepository
                        .findByMedicineId(medicineId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found"));

        inventory.setQuantity(quantity);
        inventory.setLastUpdated(
                LocalDateTime.now());

        inventory = inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    private InventoryResponse mapToResponse(
            Inventory inventory) {

        return new InventoryResponse(
                inventory.getId(),
                inventory.getMedicine().getId(),
                inventory.getMedicine().getName(),
                inventory.getQuantity()
        );
    }
    
    public boolean hasEnoughStock(
            Long medicineId,
            Integer requestedQuantity) {

        Inventory inventory = inventoryRepository
                .findByMedicineId(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found"));

        return inventory.getQuantity() >= requestedQuantity;
    }
    public void reduceStock(
            Long medicineId,
            Integer quantity) {

        Inventory inventory = inventoryRepository
                .findByMedicineId(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found"));

        if (inventory.getQuantity() < quantity) {
            throw new OutOfStockException(
                    "Insufficient stock available");
        }

        inventory.setQuantity(
                inventory.getQuantity() - quantity
        );

        inventory.setLastUpdated(
                LocalDateTime.now()
        );

        inventoryRepository.save(inventory);
    }
    public void increaseStock(
            Long medicineId,
            Integer quantity) {

        Inventory inventory = inventoryRepository
                .findByMedicineId(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found"));

        inventory.setQuantity(
                inventory.getQuantity() + quantity
        );

        inventory.setLastUpdated(
                LocalDateTime.now()
        );

        inventoryRepository.save(inventory);
    }
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public List<InventoryResponse> getLowStockMedicines() {

        return inventoryRepository.findAll()
                .stream()
                .filter(i ->
                        i.getQuantity()
                                <= i.getReorderLevel())
                .map(this::mapToResponse)
                .toList();
    }
}