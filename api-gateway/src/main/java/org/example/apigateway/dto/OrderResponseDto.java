package org.example.apigateway.dto;

import java.util.List;

public class OrderResponseDto {
    private String orderId;
    private String customerId;
    private List<OrderItemDetailDto> items;
    private double totalAmount;
    private String orderStatus;
    private String createdAt;

    public OrderResponseDto(String orderId, String customerId, List<OrderItemDetailDto> items, double totalAmount, String orderStatus, String createdAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDetailDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDetailDto> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
