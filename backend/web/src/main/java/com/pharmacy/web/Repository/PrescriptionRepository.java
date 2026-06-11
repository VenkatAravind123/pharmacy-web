package com.pharmacy.web.Repository;


import com.pharmacy.web.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {
}
