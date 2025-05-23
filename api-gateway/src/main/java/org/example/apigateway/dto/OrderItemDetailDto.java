package org.example.apigateway.dto;

public class OrderItemDetailDto {
    private ProductResponseDto productResponseDto;
    private int quantity;
    private double subtotal;

    public OrderItemDetailDto(ProductResponseDto productResponseDto, int quantity, double subtotal) {
        this.productResponseDto = productResponseDto;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public ProductResponseDto getProductResponseDto() {
        return productResponseDto;
    }

    public void setProductResponseDto(ProductResponseDto productResponseDto) {
        this.productResponseDto = productResponseDto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}