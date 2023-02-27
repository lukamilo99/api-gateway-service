package raf.rs.apigatewayservice.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    private final String openEndpoint = "m1/user/auth/";

    public boolean isOpenEndpoint(ServerHttpRequest request){
       return request.getURI().getPath().contains(openEndpoint);
    }
}
