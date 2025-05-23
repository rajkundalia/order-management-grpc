package org.example.apigateway.dto;

import java.util.List;

public class OrderRequestDto {
    private String customerId;
    private List<OrderItemDto> items;

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}