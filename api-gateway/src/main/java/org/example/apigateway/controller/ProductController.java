package org.example.apigateway.controller;

import org.example.apigateway.dto.ProductResponseDto;
import org.example.product.ProductListRequest;
import org.example.product.ProductRequest;
import org.example.product.ProductResponse;
import org.example.product.ProductServiceGrpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

    public ProductController(ProductServiceGrpc.ProductServiceBlockingStub productServiceStub) {
        this.productServiceStub = productServiceStub;
    }

    @GetMapping("/{productId}")
    public Mono<ProductResponseDto> getProduct(@PathVariable(name = "productId") String productId) {
        ProductRequest request = ProductRequest.newBuilder()
                .setProductId(productId)
                .build();

        ProductResponse response = productServiceStub.getProduct(request);
        ProductResponseDto productResponseDto = toDto(response);
        return Mono.just(productResponseDto);
    }

    @GetMapping
    public Mono<List<ProductResponseDto>> listProducts(@RequestParam(defaultValue = "10", name = "limit") int limit) {
        ProductListRequest request = ProductListRequest.newBuilder()
                .setLimit(limit)
                .build();
        List<ProductResponse> productResponseList = productServiceStub.listProducts(request).getProductsList();

        List<ProductResponseDto> productResponseDtos = productResponseList.stream()
                .map(ProductController::toDto).collect(Collectors.toList());
        return Mono.just(productResponseDtos);
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