package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.CreateOrderRequest;
import com.shopping.electronic.store.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest createOrderRequest);

    void removeOrder(String orderId);

    List<OrderDto> getAllOrdersOfUser(String userId);

    List<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
}
