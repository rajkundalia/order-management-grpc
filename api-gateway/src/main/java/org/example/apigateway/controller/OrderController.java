package org.example.apigateway.controller;

import org.example.apigateway.dto.OrderItemDetailDto;
import org.example.apigateway.dto.OrderRequestDto;
import org.example.apigateway.dto.OrderResponseDto;
import org.example.order.OrderDetailsRequest;
import org.example.order.OrderItem;
import org.example.order.OrderItemDetail;
import org.example.order.OrderRequest;
import org.example.order.OrderResponse;
import org.example.order.OrderServiceGrpc;
import org.example.product.ProductServiceGrpc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceGrpc.OrderServiceBlockingStub orderServiceStub;

    public OrderController(OrderServiceGrpc.OrderServiceBlockingStub orderServiceStub) {
        this.orderServiceStub = orderServiceStub;
    }

    @PostMapping
    public Mono<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto) {
        List<OrderItem> items = requestDto.getItems().stream()
                .map(item -> OrderItem.newBuilder()
                        .setProductId(item.getProductId())
                        .setQuantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        OrderRequest request = OrderRequest.newBuilder()
                .setCustomerId(requestDto.getCustomerId())
                .addAllItems(items)
                .build();

        OrderResponse response = orderServiceStub.createOrder(request);
        OrderResponseDto responseDto = toDto(response);
        return Mono.just(responseDto);
    }

    @GetMapping("/{orderId}")
    public Mono<OrderResponseDto> getOrderDetails(@PathVariable(name = "orderId") String orderId) {
        OrderDetailsRequest request = OrderDetailsRequest.newBuilder()
                .setOrderId(orderId)
                .build();

        OrderResponse response = orderServiceStub.getOrderDetails(request);

        OrderResponseDto responseDto = toDto(response);
        return Mono.just(responseDto);
    }

    public static OrderResponseDto toDto(OrderResponse proto) {
        List<OrderItemDetailDto> items = proto.getItemsList().stream()
                .map(OrderController::toDto)
                .collect(Collectors.toList());

        return new OrderResponseDto(
                proto.getOrderId(),
                proto.getCustomerId(),
                items,
                proto.getTotalAmount(),
                proto.getOrderStatus(),
                proto.getCreatedAt()
        );
    }

    public static OrderItemDetailDto toDto(OrderItemDetail proto) {
        return new OrderItemDetailDto(
                ProductController.toDto(proto.getProduct()),
                proto.getQuantity(),
                proto.getSubtotal()
        );
    }
}
