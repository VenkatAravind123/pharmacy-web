package com.pharmacy.web.Repository;


import com.pharmacy.web.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByOrderId(Long orderId);
}
