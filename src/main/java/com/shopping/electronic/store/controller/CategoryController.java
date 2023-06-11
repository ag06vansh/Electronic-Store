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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
@RestController
@RequestMapping("/categories")
@Tag(
        name = "Product Category",
        description = "operations related to product category management"
)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${category.image.path}")
    private String imageUploadPath;

    /**
     * Method to create new category
     *
     * @param categoryDto
     * @return
     */
    @Operation(summary = "create new product category")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody final CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    /**
     * Method to update category
     *
     * @param categoryId
     * @param categoryDto
     * @return
     */
    @Operation(summary = "update product category")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("categoryId") final String categoryId,
                                                      @Valid @RequestBody final CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    /**
     * Method to delete category
     *
     * @param categoryId
     * @return
     */
    @Operation(summary = "delete product category using categoryId")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") final String categoryId) {
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

    /**
     * Method to fetch all categories
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Operation(summary = "fetch all product categories")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) final int pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "title", required = false) final String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) final String sortDir) {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    /**
     * Method to fetch category
     *
     * @param categoryId
     * @return
     */
    @Operation(summary = "fetch product category using categoryId")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("categoryId") final String categoryId) {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * Method to search category using keyword
     *
     * @param keyword
     * @return
     */
    @Operation(summary = "search product category using keyword")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable(value = "keyword") final String keyword) {
        List<CategoryDto> categoryDtoList = categoryService.searchCategory(keyword);
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    /**
     * Method to upload category image
     *
     * @param categoryId
     * @param file
     * @return
     * @throws IOException
     */
    @Operation(summary = "upload product category image")
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@PathVariable("categoryId") final String categoryId,
                                                             @RequestPart("categoryImage") final MultipartFile file) throws IOException {
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

    /**
     * Method to fetch category image
     *
     * @param categoryId
     * @param httpServletResponse
     * @throws IOException
     */
    @Operation(summary = "fetch product category image using categoryID")
    @GetMapping("/image/{categoryId}")
    public void getCategoryImage(@PathVariable("categoryId") final String categoryId,
                                 HttpServletResponse httpServletResponse) throws IOException {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        InputStream image = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, httpServletResponse.getOutputStream());
    }

    /**
     * Method to add new product using existing category
     *
     * @param categoryId
     * @param productDto
     * @return
     */
    @Operation(summary = "add new product using existing product category")
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryId") final String categoryId,
                                                                @RequestBody final ProductDto productDto) {
        ProductDto productWithCategory = productService.createProductWithCategory(categoryId, productDto);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    /**
     * Method to update product created using existing product category
     *
     * @param categoryId
     * @param productId
     * @return
     */
    @Operation(summary = "update product created using existing product category")
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductCategory(@PathVariable("categoryId") final String categoryId,
                                                            @PathVariable("productId") final String productId) {
        ProductDto productWithCategory = productService.updateProductCategory(productId, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.OK);
    }

    /**
     * Method to fetch all products of category using categoryId
     *
     * @param categoryId
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Operation(summary = "fetch all products of category using categoryId")
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getAllProductOfCategory(@PathVariable("categoryId") final String categoryId,
                                                                    @RequestParam(value = "pageNumber", defaultValue = "0", required = false) final int pageNumber,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = "title", required = false) final String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) final String sortDir) {
        List<ProductDto> productDtoList = productService.getAllProductOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }
}
