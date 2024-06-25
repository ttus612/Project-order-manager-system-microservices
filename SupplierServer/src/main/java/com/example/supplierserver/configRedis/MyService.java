package com.example.supplierserver.configRedis;


import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.control.Try;
public class MyService {
    private final RateLimiter rateLimiter;

    public MyService() {
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.ofDefaults();
        rateLimiter = rateLimiterRegistry.rateLimiter("supplierService");
    }

    public String myMethod() {
        Try<String> result = Try.ofSupplier(
                RateLimiter.decorateSupplier(rateLimiter, () -> {
                    // Your protected code here
                    return "Result of protected call";
                })
        ).recover(throwable -> {
            if (throwable instanceof RequestNotPermitted) {
                // Fallback action when request is not permitted
                return "Fallback response";
            } else {
                // Handle other exceptions
                return "Error occurred: " + throwable.getMessage();
            }
        });

        return result.get();
    }
}