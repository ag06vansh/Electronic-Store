package com.shopping.electronic.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    private String cartId;
    private String userId;
    private String orderStatus = "PENDING";
    private String paymentStatus = "NOT-PAID";
    private String billingAddress;
    private String billingPhone;
    private String billingName;
}
