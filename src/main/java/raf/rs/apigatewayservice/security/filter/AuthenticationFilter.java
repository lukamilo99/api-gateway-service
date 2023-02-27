package raf.rs.apigatewayservice.security.filter;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import raf.rs.apigatewayservice.config.RouteValidator;
import raf.rs.apigatewayservice.security.jwt.JwtUtils;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class AuthenticationFilter implements GatewayFilter {

    private RouteValidator routeValidator;
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = getTokenFromRequest(request);

        if(routeValidator.isOpenEndpoint(request)) return chain.filter(exchange);

        else if(jwtUtils.isTokenValid(token)){
           return chain.filter(exchange);
        }
        else return onError(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }

    private String getTokenFromRequest(ServerHttpRequest request){
        String bearerToken = request.getHeaders().getFirst("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return "";
    }
}
