package dev.macako.homeinventory.homeinventoryapigateway;

import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class LoggingFilter implements GlobalFilter {

  private final Logger logger = getLogger(LoggingFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    logger.info("Path of the request received -> {}", exchange.getRequest().getPath());
    return chain.filter(exchange);
  }
}
