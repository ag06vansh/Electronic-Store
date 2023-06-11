package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.AddItemToCartRequest;
import com.shopping.electronic.store.dto.CartDto;
import com.shopping.electronic.store.exception.ResourceNotFoundException;
import com.shopping.electronic.store.model.Cart;
import com.shopping.electronic.store.model.CartItem;
import com.shopping.electronic.store.model.Product;
import com.shopping.electronic.store.model.User;
import com.shopping.electronic.store.repository.CartItemRepository;
import com.shopping.electronic.store.repository.CartRepository;
import com.shopping.electronic.store.repository.ProductRepository;
import com.shopping.electronic.store.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }

        AtomicBoolean updated = new AtomicBoolean(false);
        cart.getItems().stream().map(item -> {
            if (item.getProduct().getProductId().equals(request.getProductId())) {
                item.setQuantity(request.getQuantity());
                item.setTotalPrice(request.getQuantity() * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        //create cart item
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(request.getQuantity())
                    .totalPrice(request.getQuantity() * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user);
        return modelMapper.map(cart, CartDto.class);
    }
}
