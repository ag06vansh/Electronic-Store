package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.CreateOrderRequest;
import com.shopping.electronic.store.dto.OrderDto;
import com.shopping.electronic.store.exception.BadApiRequestException;
import com.shopping.electronic.store.exception.ResourceNotFoundException;
import com.shopping.electronic.store.model.Cart;
import com.shopping.electronic.store.model.CartItem;
import com.shopping.electronic.store.model.Order;
import com.shopping.electronic.store.model.OrderItem;
import com.shopping.electronic.store.model.User;
import com.shopping.electronic.store.repository.CartRepository;
import com.shopping.electronic.store.repository.OrderRepository;
import com.shopping.electronic.store.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest createOrderRequest) {
        User user = userRepository.findById(createOrderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findById(createOrderRequest.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size() <= 0) {
            throw new BadApiRequestException("Invalid number of items in cart !!!");
        }
        Order order = Order.builder()
                .billingName(createOrderRequest.getBillingName())
                .billingPhone(createOrderRequest.getBillingPhone())
                .billingAddress(createOrderRequest.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(createOrderRequest.getPaymentStatus())
                .orderStatus(createOrderRequest.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            // CartItem -> OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found !!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getAllOrdersOfUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtoList = orders.stream().map(order ->
                modelMapper.map(order, OrderDto.class)
        ).collect(Collectors.toList());
        return orderDtoList;
    }

    @Override
    public List<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals("ASC") ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Order> orders = orderRepository.findAll(pageable).toList();
        List<OrderDto> orderDtoList = orders.stream().map(order ->
                modelMapper.map(order, OrderDto.class)
        ).collect(Collectors.toList());
        return orderDtoList;
    }
}
