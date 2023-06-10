package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(String categoryId, CategoryDto categoryDto);

    void deleteCategory(String categoryId);

    List<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

    CategoryDto getCategory(String categoryId);

    List<CategoryDto> searchCategory(String keyword);

}
