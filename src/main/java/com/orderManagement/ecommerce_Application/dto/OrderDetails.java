package com.orderManagement.ecommerce_Application.dto;

import lombok.Data;

@Data
public class OrderDetails {
    private String orderId;
    private String shippingAddress;
    private String email;
}
