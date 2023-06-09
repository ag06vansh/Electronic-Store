package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.AddItemToCartRequest;
import com.shopping.electronic.store.dto.CartDto;

public interface CartService {

    // Add items to cart
    // Case1: cart for user not available so will create new cart and then add item to it
    // Case2: cart already available for user so add items to it
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    // Remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    // Remove all cart items
    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
