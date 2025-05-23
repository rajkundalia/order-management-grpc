package org.example.apigateway.config;

import org.example.order.OrderServiceGrpc;
import org.example.product.ProductServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productServiceStub(GrpcChannelUtil grpcChannelUtil) {
        return ProductServiceGrpc.newBlockingStub(grpcChannelUtil.getChannelForService("product-service"));
    }

    @Bean
    public OrderServiceGrpc.OrderServiceBlockingStub orderServiceStub(GrpcChannelUtil grpcChannelUtil) {
        return OrderServiceGrpc.newBlockingStub(grpcChannelUtil.getChannelForService("order-service"));
    }
}