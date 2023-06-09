package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.CategoryDto;
import com.shopping.electronic.store.dto.ProductDto;
import com.shopping.electronic.store.exception.ResourceNotFoundException;
import com.shopping.electronic.store.model.Category;
import com.shopping.electronic.store.model.Product;
import com.shopping.electronic.store.repository.CategoryRepository;
import com.shopping.electronic.store.repository.ProductRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${product.image.path}")
    private String imageUploadPath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = productRepository.save(modelMapper.map(productDto, Product.class));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(String productId, ProductDto productDto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found !!!"));
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.isStock());
        product.setProductImage(productDto.getProductImage());
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found !!!"));
        String fullImagePath = imageUploadPath + product.getProductImage();
        try {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        } catch (IOException ex) {
            log.info("Product image not found in folder.");
            ex.printStackTrace();
        }
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals("ASC") ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Product> productList = productRepository.findAll(pageable).toList();
        List<ProductDto> productDtoList =
            productList
                .stream()
                .map(
                    product -> modelMapper.map(product, ProductDto.class)
                )
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public ProductDto getProduct(String productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found !!!"));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllLiveProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals("ASC") ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Product> productList = productRepository.findByLiveTrue(pageable).toList();
        List<ProductDto> productDtoList =
            productList
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public List<ProductDto> getAllStockProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals("ASC") ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Product> productList = productRepository.findByStockTrue(pageable).toList();
        List<ProductDto> productDtoList =
            productList
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public List<ProductDto> searchProduct(String keyword) {
        List<Product> productList = productRepository.findByTitleContaining(keyword);
        List<ProductDto> productDtoList =
            productList
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public ProductDto createProductWithCategory(String categoryId, ProductDto productDto) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        productDto.setCategory(modelMapper.map(category, CategoryDto.class));
        Product product = productRepository.save(modelMapper.map(productDto, Product.class));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProductCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProductOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        Sort sort = sortDir.equals("ASC") ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Product> productList = productRepository.findByCategory(category, pageable).toList();
        List<ProductDto> productDtoList =
            productList
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productDtoList;
    }
}
