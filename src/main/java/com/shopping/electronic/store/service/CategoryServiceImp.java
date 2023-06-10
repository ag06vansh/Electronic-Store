package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.CategoryDto;
import com.shopping.electronic.store.exception.ResourceNotFoundException;
import com.shopping.electronic.store.model.Category;
import com.shopping.electronic.store.repository.CategoryRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${category.image.path}")
    private String imageUploadPath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(String categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found !!!"));
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found !!!"));
        String fullImagePath = imageUploadPath + category.getCoverImage();
        try {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        } catch (IOException ex) {
            log.info("Product image not found in folder.");
            ex.printStackTrace();
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals("ASC") ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        List<Category> categoryList = page.toList();
        List<CategoryDto> categoryDtoList =
                categoryList
                        .stream()
                        .map(
                                category -> modelMapper.map(category, CategoryDto.class)
                        )
                        .collect(Collectors.toList());
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found !!!"));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> categoryList = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> categoryDtoList =
                categoryList
                        .stream()
                        .map(
                                category -> modelMapper.map(category, CategoryDto.class)
                        )
                        .collect(Collectors.toList());
        return categoryDtoList;
    }
}
