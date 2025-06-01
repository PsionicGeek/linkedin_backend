package org.psionicgeek.linkedin.apigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.psionicgeek.linkedin.apigateway.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    private final JwtService jwtService;


    public static class Config {
        // Configuration properties for the filter can be added here
    }

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            log.info("Login request: {}", exchange.getRequest().getURI());
            final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();

            }

            final String token = authHeader.split("Bearer ")[1].trim();

            try {
                Long userId = jwtService.getUserIdFromToken(token);
                log.info("Authenticated user ID: {}", userId);
                exchange = exchange.mutate().request(r->r.header("X-User-Id", String.valueOf(userId))).build();
            } catch (Exception e) {
                log.error("Invalid token: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return  chain.filter(exchange);
        };
    }
}
