package com.pharmacy.web.controller;

import com.pharmacy.web.dto.PrescriptionResponse;
import com.pharmacy.web.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/upload")
    public PrescriptionResponse uploadPrescription(

            @RequestParam Long orderId,

            @RequestParam MultipartFile file)

            throws Exception {

        return prescriptionService
                .uploadPrescription(
                        orderId,
                        file);
    }

    @PutMapping("/{id}/approve")
    public PrescriptionResponse approvePrescription(
            @PathVariable Long id) {

        return prescriptionService
                .approvePrescription(id);
    }

    @PutMapping("/{id}/reject")
    public PrescriptionResponse rejectPrescription(
            @PathVariable Long id) {

        return prescriptionService
                .rejectPrescription(id);
    }
}