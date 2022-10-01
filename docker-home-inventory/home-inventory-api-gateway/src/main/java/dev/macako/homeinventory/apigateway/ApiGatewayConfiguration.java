package dev.macako.homeinventory.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

  @Bean
  public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            p ->
                p.path("/get")
                    .filters(
                        f ->
                            f.addRequestHeader("MyHeader", "MyURI")
                                .addRequestParameter("Param", "MyValue"))
                    .uri("http://httpbin.org:80"))
        .route(p -> p.path("/items/**").uri("lb://home-inventory-item-service"))
        .route(p -> p.path("/users/**").uri("lb://home-inventory-user-service"))
        .route(
            p ->
                p.path("/v2/items/**")
                    .filters(
                        f -> f.rewritePath("/v2/items/(?<segment>.*)", "/items-feign/${segment}"))
                    .uri("lb://home-inventory-item-service"))
        .build();
  }
}
