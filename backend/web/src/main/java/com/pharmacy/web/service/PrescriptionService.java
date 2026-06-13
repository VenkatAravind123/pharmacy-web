package com.pharmacy.web.service;



import com.pharmacy.web.dto.PrescriptionResponse;
import com.pharmacy.web.entity.*;
import com.pharmacy.web.exception.ResourceNotFoundException;
import com.pharmacy.web.Repository.OrderRepository;
import com.pharmacy.web.Repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final OrderRepository orderRepository;
    private final FileStorageService fileStorageService;

    public PrescriptionResponse uploadPrescription(
            Long orderId,
            MultipartFile file)
            throws Exception {

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        String filePath =
                fileStorageService.saveFile(file);

        Prescription prescription =
                Prescription.builder()
                        .fileName(
                                file.getOriginalFilename())
                        .filePath(filePath)
                        .status(
                                PrescriptionStatus.PENDING)
                        .order(order)
                        .build();

        prescription =
                prescriptionRepository.save(
                        prescription);

        return mapToResponse(prescription);
    }

    public PrescriptionResponse approvePrescription(
            Long prescriptionId) {

        Prescription prescription =
                prescriptionRepository
                        .findById(prescriptionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found"));

        prescription.setStatus(
                PrescriptionStatus.APPROVED);

        Order order =
                prescription.getOrder();

        order.setStatus(
                OrderStatus.APPROVED);

        prescription =
                prescriptionRepository.save(
                        prescription);

        return mapToResponse(prescription);
    }

    public PrescriptionResponse rejectPrescription(
            Long prescriptionId) {

        Prescription prescription =
                prescriptionRepository
                        .findById(prescriptionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found"));

        prescription.setStatus(
                PrescriptionStatus.REJECTED);

        prescription =
                prescriptionRepository.save(
                        prescription);

        return mapToResponse(prescription);
    }

    private PrescriptionResponse mapToResponse(
            Prescription prescription) {

        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getFileName(),
                prescription.getStatus().name()
        );
    }
}
