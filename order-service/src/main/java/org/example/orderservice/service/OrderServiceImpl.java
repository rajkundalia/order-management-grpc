package org.example.orderservice.service;

import io.grpc.stub.StreamObserver;
import org.example.order.OrderDetailsRequest;
import org.example.order.OrderItem;
import org.example.order.OrderItemDetail;
import org.example.order.OrderRequest;
import org.example.order.OrderResponse;
import org.example.order.OrderServiceGrpc;
import org.example.product.ProductRequest;
import org.example.product.ProductResponse;
import org.example.product.ProductServiceGrpc;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@GrpcService
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    // In-memory order database for demo
    private final Map<String, OrderResponse> orderDatabase = new ConcurrentHashMap<>();
    private final AtomicLong orderIdCounter = new AtomicLong(1000);

    // gRPC client to call Product Service
    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

    public OrderServiceImpl(ProductServiceGrpc.ProductServiceBlockingStub productServiceStub) {
        this.productServiceStub = productServiceStub;
    }

    @Override
    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        String orderId = "ORD-" + orderIdCounter.incrementAndGet();
        String customerId = request.getCustomerId();
        
        // Process order items by fetching product details from Product Service
        List<OrderItemDetail> orderItemDetails = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItem item : request.getItemsList()) {
            // Call Product Service to get product details
            ProductRequest productRequest = ProductRequest.newBuilder()
                    .setProductId(item.getProductId())
                    .build();
            
            ProductResponse productResponse = productServiceStub.getProduct(productRequest);
            
            // Calculate subtotal for this item
            double subtotal = productResponse.getPrice() * item.getQuantity();
            totalAmount += subtotal;
            
            // Create order item detail with the full product response
            OrderItemDetail orderItemDetail = OrderItemDetail.newBuilder()
                    .setProduct(productResponse)  // Using the entire product response here
                    .setQuantity(item.getQuantity())
                    .setSubtotal(subtotal)
                    .build();
            
            orderItemDetails.add(orderItemDetail);
        }
        
        // Create the order response
        OrderResponse orderResponse = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setCustomerId(customerId)
                .addAllItems(orderItemDetails)
                .setTotalAmount(totalAmount)
                .setOrderStatus("CREATED")
                .setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
        
        // Save to in-memory database
        orderDatabase.put(orderId, orderResponse);
        
        // Send response
        responseObserver.onNext(orderResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderDetails(OrderDetailsRequest request, StreamObserver<OrderResponse> responseObserver) {
        String orderId = request.getOrderId();
        OrderResponse orderResponse = orderDatabase.get(orderId);
        
        if (orderResponse != null) {
            responseObserver.onNext(orderResponse);
        } else {
            // Return empty order if not found
            OrderResponse emptyOrder = OrderResponse.newBuilder()
                    .setOrderId(orderId)
                    .setCustomerId("")
                    .setTotalAmount(0)
                    .setOrderStatus("NOT_FOUND")
                    .setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .build();
            responseObserver.onNext(emptyOrder);
        }
        
        responseObserver.onCompleted();
    }
}