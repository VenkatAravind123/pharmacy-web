package com.pharmacy.web.controller;



import com.pharmacy.web.dto.*;
import com.pharmacy.web.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    public MedicineResponse createMedicine(
            @Valid
            @RequestBody MedicineRequest request) {

        return medicineService.createMedicine(request);
    }

    @GetMapping
    public List<MedicineResponse> getAllMedicines() {

        return medicineService.getAllMedicines();
    }

    @GetMapping("/{id}")
    public MedicineResponse getMedicineById(
            @PathVariable Long id) {

        return medicineService.getMedicineById(id);
    }

    @GetMapping("/search")
    public List<MedicineResponse> searchMedicine(
            @RequestParam String keyword) {

        return medicineService.searchMedicine(keyword);
    }

    @DeleteMapping("/{id}")
    public String deleteMedicine(
            @PathVariable Long id) {

        medicineService.deleteMedicine(id);

        return "Medicine Deleted Successfully";
    }
    
    @PutMapping("/{id}")
    public MedicineResponse updateMedicine(
            @PathVariable Long id,
            @Valid
            @RequestBody MedicineRequest request) {

        return medicineService.updateMedicine(
                id,
                request);
    } 
}