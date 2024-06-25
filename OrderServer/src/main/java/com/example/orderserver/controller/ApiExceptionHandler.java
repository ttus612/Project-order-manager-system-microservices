package com.example.orderserver.controller;


import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({ RequestNotPermitted.class })
    public ResponseEntity<String> handleRequestNotPermitted() {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("[RateLimiterService - OrderServer] Please wait 20 seconds to request a new request");
    }
}
