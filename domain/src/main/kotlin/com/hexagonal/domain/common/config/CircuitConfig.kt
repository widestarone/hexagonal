package com.hexagonal.domain.common.config

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CircuitConfig(
    var circuitBreakerRegistry: CircuitBreakerRegistry,
) {
    @Bean
    fun defaultCircuitBreaker(): CircuitBreaker = circuitBreakerRegistry.circuitBreaker("default")

    @Bean
    fun mediaProcessingCircuitBreaker(): CircuitBreaker = circuitBreakerRegistry.circuitBreaker("mp")
}
