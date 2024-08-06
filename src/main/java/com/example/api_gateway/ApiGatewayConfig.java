package com.example.api_gateway;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.netty.http.client.HttpClient;

@CrossOrigin(origins = "*")
@Configuration
public class ApiGatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r.path("/api/products/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://product-service"))
                .route("user-service", r -> r.path("/api/users/**", "/api/auth/signin", "/api/auth/signup")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .build();
    }
}
