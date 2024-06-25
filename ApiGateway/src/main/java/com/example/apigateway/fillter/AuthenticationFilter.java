package com.example.apigateway.fillter;

import com.example.apigateway.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;

//    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    private JWTUtil jwtUtil = new JWTUtil();
    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = null;
            if (validator.isSecured.test(exchange.getRequest())){
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Authorization header not found");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try{
//                    restTemplate.getForObject("http://JWTServer//validate?token"+authHeader, String.class);
                    jwtUtil.validateToken(authHeader);

                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", jwtUtil.extractUsername(authHeader))
                            .build();
                }catch (Exception e){
                    System.out.println("Invalid access ... ");
                    throw new RuntimeException("Un authorized access to application");
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }
}
