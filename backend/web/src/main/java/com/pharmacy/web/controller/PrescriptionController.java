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
    private final com.pharmacy.web.Repository.PrescriptionRepository prescriptionRepository;
    private final com.pharmacy.web.service.FileStorageService fileStorageService;

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

    @GetMapping("/{id}/download")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> downloadPrescription(@PathVariable Long id) throws Exception {
        com.pharmacy.web.entity.Prescription p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        org.springframework.core.io.Resource resource = fileStorageService.loadFileAsResource(p.getFilePath());
        
        String contentType = "application/octet-stream";
        if(p.getFileName().toLowerCase().endsWith(".png")) contentType = "image/png";
        else if(p.getFileName().toLowerCase().endsWith(".jpg") || p.getFileName().toLowerCase().endsWith(".jpeg")) contentType = "image/jpeg";
        else if(p.getFileName().toLowerCase().endsWith(".pdf")) contentType = "application/pdf";

        return org.springframework.http.ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + p.getFileName() + "\"")
                .body(resource);
    }
}