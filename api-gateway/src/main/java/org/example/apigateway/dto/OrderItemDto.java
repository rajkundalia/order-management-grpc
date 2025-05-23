package org.example.apigateway.dto;

import org.example.product.ProductResponse;

public class OrderItemDto {
    private String productId;
    private int quantity;

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static ProductResponseDto toDto(ProductResponse response) {
        return new ProductResponseDto(
                response.getProductId(),
                response.getName(),
                response.getDescription(),
                response.getPrice(),
                response.getStockQuantity()
        );
    }
}