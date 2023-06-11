package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.CreateOrderRequest;
import com.shopping.electronic.store.dto.OrderDto;
import com.shopping.electronic.store.service.OrderService;
import com.shopping.electronic.store.util.ApiResponse;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(
        name = "Order",
        description = "operations related to user shopping cart order management"
)
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Method to create new order
     *
     * @param createOrderRequest
     * @return
     */
    @Operation(summary = "create new order")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody final CreateOrderRequest createOrderRequest) {
        OrderDto orderDto = orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    /**
     * Method to remove order using orderId
     *
     * @param orderId
     * @return
     */
    @Operation(summary = "remove order using orderId")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> removeOrder(@PathVariable final String orderId) {
        orderService.removeOrder(orderId);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Order Removed Successfully !!!")
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Method to fetch all orders of user using userId
     *
     * @param userId
     * @return
     */
    @Operation(summary = "fetch all orders of user using userId")
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrdersOfUser(@PathVariable final String userId) {
        List<OrderDto> orders = orderService.getAllOrdersOfUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    /**
     * Method to fetch all orders of all users
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Operation(summary = "fetch all orders details of all users")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) final int pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) final String sortBy,
                                                       @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) final String sortDir) {
        List<OrderDto> orders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }
}
