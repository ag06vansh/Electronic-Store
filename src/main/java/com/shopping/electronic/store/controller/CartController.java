package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.AddItemToCartRequest;
import com.shopping.electronic.store.dto.CartDto;
import com.shopping.electronic.store.service.CartService;
import com.shopping.electronic.store.util.ApiResponse;
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
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request,
                                                 @PathVariable("userId") String userId) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable String userId,
                                                          @PathVariable int itemId) {
        cartService.removeItemFromCart(userId, itemId);
        ApiResponse apiResponse = ApiResponse.builder()
            .message("Item is removed !!!")
            .success(true)
            .status(HttpStatus.OK)
            .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponse apiResponse = ApiResponse.builder()
            .message("Cart Reset !!!")
            .success(true)
            .status(HttpStatus.OK)
            .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
