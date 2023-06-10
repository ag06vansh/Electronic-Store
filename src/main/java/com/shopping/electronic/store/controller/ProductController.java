package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.ProductDto;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imageUploadPath;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto productDto1 = productService.createProduct(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "productId") String productId,
                                                    @Valid @RequestBody ProductDto productDto) {
        ProductDto productDto1 = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable(name = "productId") String productId) {
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

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        List<ProductDto> productDtoList = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") String productId) {
        ProductDto productDto = productService.getProduct(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<List<ProductDto>> getAllLiveProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                              @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        List<ProductDto> productDtoList = productService.getAllLiveProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    @GetMapping("/stock")
    public ResponseEntity<List<ProductDto>> getAllStockProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                               @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                               @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        List<ProductDto> productDtoList = productService.getAllStockProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ProductDto>> searchProduct(@PathVariable("keyword") String keyword) {
        List<ProductDto> productDtoList = productService.searchProduct(keyword);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable("productId") String productId,
                                                            @RequestParam("productImage") MultipartFile image) throws IOException {
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

    @GetMapping("/image/{productId}")
    public void getProductImage(@PathVariable("productId") String productId,
                                HttpServletResponse httpServletResponse) throws IOException {
        ProductDto productDto = productService.getProduct(productId);
        InputStream image = fileService.getResource(imageUploadPath, productDto.getProductImage());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, httpServletResponse.getOutputStream());
    }
}
