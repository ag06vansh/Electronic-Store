package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(String productId, ProductDto productDto);

    void deleteProduct(String productId);

    List<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto getProduct(String productId);

    List<ProductDto> getAllLiveProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    List<ProductDto> getAllStockProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    List<ProductDto> searchProduct(String keyword);

    ProductDto createProductWithCategory(String categoryId, ProductDto productDto);

    ProductDto updateProductCategory(String productId, String categoryId);

    List<ProductDto> getAllProductOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
