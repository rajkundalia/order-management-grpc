package org.example.orderservice.config;

import org.example.product.ProductServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productServiceStub(GrpcChannelUtil grpcChannelUtil) {
        return ProductServiceGrpc.newBlockingStub(grpcChannelUtil.getChannelForService("product-service"));
    }
}