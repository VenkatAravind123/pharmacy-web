package com.pharmacy.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.web.entity.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
}
