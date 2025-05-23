package org.example.orderservice.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcChannelUtil {

    private final EurekaClient eurekaClient;

    public GrpcChannelUtil(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    public ManagedChannel getChannelForService(String serviceName) {
        // Eureka service names are usually uppercase
        InstanceInfo instance = eurekaClient.getNextServerFromEureka(serviceName.toUpperCase(), false);
        String host = instance.getHostName();
        String grpcPortStr = instance.getMetadata().get("grpc.port");
        int grpcPort = Integer.parseInt(grpcPortStr);

        return ManagedChannelBuilder.forAddress(host, grpcPort)
                .usePlaintext()
                .build();
    }
}