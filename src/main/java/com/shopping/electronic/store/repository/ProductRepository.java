package com.shopping.electronic.store.repository;

import com.shopping.electronic.store.model.Category;
import com.shopping.electronic.store.model.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByStockTrue(Pageable pageable);

    List<Product> findByTitleContaining(String keyword);

    Page<Product> findByCategory(Category category, Pageable pageable);
}
