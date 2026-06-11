package com.pharmacy.web.Repository;


import com.pharmacy.web.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByMedicineId(Long medicineId);
}
