package org.example.productservice.service;

import io.grpc.stub.StreamObserver;
import org.example.product.ProductListRequest;
import org.example.product.ProductListResponse;
import org.example.product.ProductRequest;
import org.example.product.ProductResponse;
import org.example.product.ProductServiceGrpc;
import org.springframework.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    // In-memory product database for demo
    private final Map<String, ProductResponse> productDatabase = new HashMap<>();

    public ProductServiceImpl() {
        // Initialize with sample products
        addProduct("P001", "Smartphone X", "Latest smartphone with high-end features", 899.99, 50);
        addProduct("P002", "Laptop Pro", "Professional laptop for developers", 1299.99, 30);
        addProduct("P003", "Wireless Earbuds", "Premium sound quality wireless earbuds", 149.99, 100);
        addProduct("P004", "Smart Watch", "Fitness and health tracking smart watch", 249.99, 75);
        addProduct("P005", "Tablet Ultra", "Ultra-thin tablet with long battery life", 499.99, 45);
    }

    private void addProduct(String id, String name, String description, double price, int stockQuantity) {
        ProductResponse product = ProductResponse.newBuilder()
                .setProductId(id)
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .setStockQuantity(stockQuantity)
                .build();
        productDatabase.put(id, product);
    }

    @Override
    public void getProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        String productId = request.getProductId();
        ProductResponse product = productDatabase.getOrDefault(productId,
                ProductResponse.newBuilder()
                        .setProductId(productId)
                        .setName("Unknown Product")
                        .setDescription("Product not found")
                        .setPrice(0)
                        .setStockQuantity(0)
                        .build());

        responseObserver.onNext(product);
        responseObserver.onCompleted();
    }

    @Override
    public void listProducts(ProductListRequest request, StreamObserver<ProductListResponse> responseObserver) {
        int limit = Math.min(request.getLimit(), productDatabase.size());
        List<ProductResponse> products = new ArrayList<>(productDatabase.values());

        ProductListResponse response = ProductListResponse.newBuilder()
                .addAllProducts(products.subList(0, limit))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}