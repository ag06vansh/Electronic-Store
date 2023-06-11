package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.AddItemToCartRequest;
import com.shopping.electronic.store.dto.CartDto;
import com.shopping.electronic.store.service.CartService;
import com.shopping.electronic.store.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@Tag(
        name = "User Shopping Cart",
        description = "operations related to user shopping cart management"
)
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Method to add item to cart
     *
     * @param request
     * @param userId
     * @return
     */
    @Operation(summary = "add new item to user shopping cart")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody final AddItemToCartRequest request,
                                                 @PathVariable("userId") final String userId) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    /**
     * Method to delete item from cart
     *
     * @param userId
     * @param itemId
     * @return
     */
    @Operation(summary = "delete item from user shopping cart using userId and itemId")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable final String userId,
                                                          @PathVariable final int itemId) {
        cartService.removeItemFromCart(userId, itemId);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Item is removed !!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Method to reset cart
     *
     * @param userId
     * @return
     */
    @Operation(summary = "remove all items from user shopping cart using userId")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable final String userId) {
        cartService.clearCart(userId);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Cart Reset !!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Method to view cart
     *
     * @param userId
     * @return
     */
    @Operation(summary = "view user shopping cart details by userId")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable final String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
