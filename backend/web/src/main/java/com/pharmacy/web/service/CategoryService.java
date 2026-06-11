package com.pharmacy.web.service;



import com.pharmacy.web.dto.*;
import com.pharmacy.web.entity.Category;
import com.pharmacy.web.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(
            CategoryRequest request) {

        if(categoryRepository.existsByName(request.name())) {
            throw new RuntimeException("Category already exists");
        }

        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .build();

        category = categoryRepository.save(category);

        return mapToResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        category.setName(request.name());
        category.setDescription(request.description());

        category = categoryRepository.save(category);

        return mapToResponse(category);
    }

    public void deleteCategory(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }

    private CategoryResponse mapToResponse(
            Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}