package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.ProductDto;
import com.shopping.electronic.store.service.FileService;
import com.shopping.electronic.store.service.ProductService;
import com.shopping.electronic.store.util.ApiResponse;
import com.shopping.electronic.store.util.ImageResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/products")
@Tag(
        name = "Electronic Products",
        description = "operations related to product management"
)
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imageUploadPath;

    /**
     * Method to add new product
     *
     * @param productDto
     * @return
     */
    @Operation(summary = "add new product details")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody final ProductDto productDto) {
        ProductDto productDto1 = productService.createProduct(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }

    /**
     * Method to update product details
     *
     * @param productId
     * @param productDto
     * @return
     */
    @Operation(summary = "update product details")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "productId") final String productId,
                                                    @Valid @RequestBody final ProductDto productDto) {
        ProductDto productDto1 = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    /**
     * Method to delete product details using productId
     *
     * @param productId
     * @return
     */
    @Operation(summary = "delete product details using productId")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable(name = "productId") final String productId) {
        productService.deleteProduct(productId);
        ApiResponse apiResponse =
                ApiResponse
                        .builder()
                        .status(HttpStatus.OK)
                        .success(true)
                        .message("Product deleted successfully !!!")
                        .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Method to fetch all products details
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Operation(summary = "fetch all products details")
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) final int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "title", required = false) final String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) final String sortDir) {
        List<ProductDto> productDtoList = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    /**
     * Method to fetch product details using productId
     *
     * @param productId
     * @return
     */
    @Operation(summary = "fetch product details using productId")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") final String productId) {
        ProductDto productDto = productService.getProduct(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * Method to fetch all products details which are live
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Operation(summary = "fetch all products details which are live")
    @GetMapping("/live")
    public ResponseEntity<List<ProductDto>> getAllLiveProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) final int pageNumber,
                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = "title", required = false) final String sortBy,
                                                              @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) final String sortDir) {
        List<ProductDto> productDtoList = productService.getAllLiveProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    /**
     * Method to fetch all products details which are in stock
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Operation(summary = "fetch all products details which are in stock")
    @GetMapping("/stock")
    public ResponseEntity<List<ProductDto>> getAllStockProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) final int pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
                                                               @RequestParam(value = "sortBy", defaultValue = "title", required = false) final String sortBy,
                                                               @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) final String sortDir) {
        List<ProductDto> productDtoList = productService.getAllStockProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    /**
     * Method to search product using keyword
     *
     * @param keyword
     * @return
     */
    @Operation(summary = "search product details user keyword")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ProductDto>> searchProduct(@PathVariable("keyword") final String keyword) {
        List<ProductDto> productDtoList = productService.searchProduct(keyword);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    /**
     * Method to upload product image
     *
     * @param productId
     * @param image
     * @return
     * @throws IOException
     */
    @Operation(summary = "upload product image")
    @PostMapping(value = "/image/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable("productId") final String productId,
                                                            @RequestParam("productImage") final MultipartFile image) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        // saving image name with user data
        ProductDto productDto = productService.getProduct(productId);
        productDto.setProductImage(imageName);
        productService.updateProduct(productId, productDto);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .message("User Image Uploaded Successfully !")
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * Method to fetch product image using productId
     *
     * @param productId
     * @param httpServletResponse
     * @throws IOException
     */
    @Operation(summary = "fetch product image using productId")
    @GetMapping(value = "/image/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getProductImage(@PathVariable("productId") final String productId,
                                HttpServletResponse httpServletResponse) throws IOException {
        ProductDto productDto = productService.getProduct(productId);
        InputStream image = fileService.getResource(imageUploadPath, productDto.getProductImage());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, httpServletResponse.getOutputStream());
    }
}
