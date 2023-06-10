package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.CategoryDto;
import com.shopping.electronic.store.dto.ProductDto;
import com.shopping.electronic.store.service.CategoryService;
import com.shopping.electronic.store.service.FileService;
import com.shopping.electronic.store.service.ProductService;
import com.shopping.electronic.store.util.ApiResponse;
import com.shopping.electronic.store.util.ImageResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${category.image.path}")
    private String imageUploadPath;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("categoryId") String categoryId,
                                                      @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponse apiResponse =
                ApiResponse
                        .builder()
                        .message("Category is deleted succesfully !!!")
                        .status(HttpStatus.OK)
                        .success(true)
                        .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("categoryId") String categoryId) {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable(value = "keyword") String keyword) {
        List<CategoryDto> categoryDtoList = categoryService.searchCategory(keyword);
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@PathVariable("categoryId") String categoryId,
                                                             @RequestPart("categoryImage") MultipartFile file) throws IOException {
        String imageName = fileService.uploadFile(file, imageUploadPath);
        // saving image name with category data
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        categoryDto.setCoverImage(imageName);
        categoryService.updateCategory(categoryId, categoryDto);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .message("Category Image Uploaded Successfully !")
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void getCategoryImage(@PathVariable("categoryId") String categoryId,
                                 HttpServletResponse httpServletResponse) throws IOException {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        InputStream image = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, httpServletResponse.getOutputStream());
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryId") String categoryId,
                                                                @RequestBody ProductDto productDto) {
        ProductDto productWithCategory = productService.createProductWithCategory(categoryId, productDto);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductCategory(@PathVariable("categoryId") String categoryId,
                                                            @PathVariable("productId") String productId) {
        ProductDto productWithCategory = productService.updateProductCategory(productId, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getAllProductOfCategory(@PathVariable("categoryId") String categoryId,
                                                                    @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        List<ProductDto> productDtoList = productService.getAllProductOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }
}
