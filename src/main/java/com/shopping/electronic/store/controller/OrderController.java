package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.CreateOrderRequest;
import com.shopping.electronic.store.dto.OrderDto;
import com.shopping.electronic.store.service.OrderService;
import com.shopping.electronic.store.util.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        OrderDto orderDto = orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        ApiResponse apiResponse = ApiResponse.builder()
            .success(true)
            .status(HttpStatus.OK)
            .message("Order Removed Successfully !!!")
            .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrdersOfUser(@PathVariable String userId) {
        List<OrderDto> orders = orderService.getAllOrdersOfUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
                                                       @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        List<OrderDto> orders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }
}
