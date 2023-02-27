package raf.rs.apigatewayservice.config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import raf.rs.apigatewayservice.security.filter.AuthenticationFilter;

@AllArgsConstructor
@Configuration
public class ApiGatewayConfig {

    private AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("forbidden", forbidden -> forbidden.path("/m1/inter-service/**")
                        .filters(f -> f.rewritePath("/inter-service", "/forbidden")
                                .setStatus(HttpStatus.FORBIDDEN))
                        .uri("lb://user-service"))
                .route("user-service", route -> route.path("/m1/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://user-service"))
                .route("car-service", route -> route.path("/m2/**")
                        .and()
                        .not(forbidden -> forbidden.path("m2/inter-service/**"))
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://car-service"))
                .build();
    }

}
