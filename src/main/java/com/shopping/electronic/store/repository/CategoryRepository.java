package com.shopping.electronic.store.repository;

import com.shopping.electronic.store.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByTitleContaining(String keyword);
}
