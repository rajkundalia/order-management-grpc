# Order Management System using gRPC:

I've created a complete Spring Boot project with two gRPC-enabled services that communicate with each other, along with an API Gateway and Eureka server for service discovery. 
Here's a summary of what this project demonstrates:

##Key Components

1. Eureka Server: Service registry for service discovery
2. API Gateway: Entry point that translates HTTP requests to gRPC calls
3. Product Service (gRPC Server 1): Handles product information
4. Order Service (gRPC Server 2): Processes orders by calling Product Service via gRPC
5. Proto Common: Contains the proto files

##Communication Flow

1. External clients call API Gateway via HTTP/REST
2. API Gateway forwards requests to appropriate microservices using gRPC
3. Order Service calls Product Service via gRPC when processing orders
4. All services register with Eureka Server for service discovery

##Key Features Demonstrated

1. Spring Boot gRPC server implementation
2. gRPC service-to-service communication
3. Protocol Buffers for data definition
4. Service discovery using Eureka
5. API Gateway pattern with Spring Cloud Gateway

The Product Service handles product information and provides endpoints for retrieving products. The Order Service processes orders and communicates with the Product Service via gRPC to fetch product details when creating orders.

This project structure provides a good foundation for understanding how gRPC services communicate in a Spring Boot microservices environment.

##Starting the Services
You can perform a ```mvn clean install``` at the parent folder, this will build all the jars and get java code for proto files as well.

Start the services in the following order:

1. Eureka Server: Service registry must start first
```bash
cd eureka-server
mvn spring-boot:run
```
2. Product Service: gRPC server 1
```bash
cd product-service
mvn spring-boot:run
```
3. Order Service: gRPC server 2 (calls Product Service)
```bash
cd order-service
mvn spring-boot:run
```
4. API Gateway: Entry point for clients
```bash
cd api-gateway
mvn spring-boot:run
```

##Testing the Application
1. Get Product List
```bash
curl http://localhost:8080/api/products
```
3. Get Product Details
```bash    
curl http://localhost:8080/api/products/P001
```
4. Create a New Order
```bash
   curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST001",
    "items": [
      {
        "productId": "P001",
        "quantity": 2
      },
      {
        "productId": "P003",
        "quantity": 1
      }
    ]
  }'
```
5. Get Order Details
```bash
curl http://localhost:8080/api/orders/ORD-1001
```

Disclaimer: 
Generally the usages found all over internet are: net.devh -> grpc-spring-boot-starter (from https://github.com/grpc-ecosystem/grpc-spring)
and I have used: org.springframework.grpc -> spring-grpc-spring-boot-starter (from https://docs.spring.io/spring-grpc/reference/index.html).

Also, both work.

