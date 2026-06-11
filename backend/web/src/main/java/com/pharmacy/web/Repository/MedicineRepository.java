package com.pharmacy.web.Repository;




import com.pharmacy.web.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository
        extends JpaRepository<Medicine, Long> {

    List<Medicine> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);
}