package com.github.Hugornda.vendor_ms.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class VendorAccessDeniedHandler implements ServerAccessDeniedHandler {
    Logger logger = LoggerFactory.getLogger(VendorAccessDeniedHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        logger.info("Unauthorized request - {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getMethod());
        return exchange.getResponse().setComplete();
    }
}
