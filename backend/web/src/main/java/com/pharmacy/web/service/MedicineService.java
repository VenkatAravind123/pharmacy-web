package com.pharmacy.web.service;



import com.pharmacy.web.dto.*;
import com.pharmacy.web.entity.*;
import com.pharmacy.web.exception.DuplicateResourceException;
import com.pharmacy.web.exception.ResourceNotFoundException;
import com.pharmacy.web.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final CategoryRepository categoryRepository;

    public MedicineResponse createMedicine(
            MedicineRequest request) {

        if(medicineRepository.existsByName(
                request.name())) {

            throw new DuplicateResourceException(
                    "Medicine already exists");
        }

        Category category =
                categoryRepository.findById(
                        request.categoryId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"));

        Medicine medicine = Medicine.builder()
                .name(request.name())
                .description(request.description())
                .dosage(request.dosage())
                .manufacturer(request.manufacturer())
                .price(request.price())
                .requiresPrescription(
                        request.requiresPrescription())
                .category(category)
                .build();

        medicine = medicineRepository.save(medicine);

        return mapToResponse(medicine);
    }

    public List<MedicineResponse> getAllMedicines() {

        return medicineRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MedicineResponse getMedicineById(Long id) {

        Medicine medicine =
                medicineRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medicine not found"));

        return mapToResponse(medicine);
    }

    public List<MedicineResponse> searchMedicine(
            String keyword) {

        return medicineRepository
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteMedicine(Long id) {

        Medicine medicine =
                medicineRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medicine not found"));

        medicineRepository.delete(medicine);
    }

    private MedicineResponse mapToResponse(
            Medicine medicine) {

        return new MedicineResponse(
                medicine.getId(),
                medicine.getName(),
                medicine.getDescription(),
                medicine.getDosage(),
                medicine.getManufacturer(),
                medicine.getPrice(),
                medicine.getRequiresPrescription(),
                medicine.getCategory().getId(),
                medicine.getCategory().getName()
        );
    }
    public MedicineResponse updateMedicine(
            Long id,
            MedicineRequest request) {

        Medicine medicine = medicineRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found"));

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"));

        medicine.setName(request.name());
        medicine.setDescription(request.description());
        medicine.setDosage(request.dosage());
        medicine.setManufacturer(request.manufacturer());
        medicine.setPrice(request.price());
        medicine.setRequiresPrescription(
                request.requiresPrescription());
        medicine.setCategory(category);

        medicine = medicineRepository.save(medicine);

        return mapToResponse(medicine);
    }
}