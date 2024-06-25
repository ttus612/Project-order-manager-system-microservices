package com.example.jwtserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JwtServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtServerApplication.class, args);
    }

}
