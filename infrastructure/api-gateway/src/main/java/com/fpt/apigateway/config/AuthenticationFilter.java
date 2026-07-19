package com.fpt.apigateway.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtils jwtUtils;


    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/api/customers/register",
            "/api/customers/login",

            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/webjars",
            "/customer-service/v3/api-docs",
            "/car-service/v3/api-docs",
            "/renting-service/v3/api-docs"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Bỏ qua preflight request của CORS (OPTIONS)
        if (request.getMethod().name().equals("OPTIONS")) {
            return chain.filter(exchange);
        }

        // không cần token
        boolean isSecured = OPEN_API_ENDPOINTS.stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));

        if (!isSecured) {
            return chain.filter(exchange);
        }

        // cần token
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, "Thiếu Header Authorization!", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Token phải bắt đầu bằng Bearer!", HttpStatus.UNAUTHORIZED);
        }

        // lấy chuỗi Token
        String token = authHeader.substring(7);

        try {
            jwtUtils.validateToken(token);

            Claims claims = jwtUtils.extractAllClaims(token);
            String role = claims.get("role", String.class);
            Long userId = claims.get("userId", Long.class);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Role", role)
                    .header("X-User-Id", userId != null ? String.valueOf(userId) : "")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("Lỗi giải mã Token: {}", e.getMessage());
            return onError(exchange, "Token không hợp lệ hoặc đã hết hạn!", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}