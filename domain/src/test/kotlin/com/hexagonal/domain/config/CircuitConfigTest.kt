package com.hexagonal.domain.config

import com.hexagonal.domainTestApplication
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")@ContextConfiguration(classes = [DomainTestApplication::class])
@EnableAutoConfiguration
@SpringBootTest
internal class CircuitConfigTest(
    @Autowired var circuitBreakerRegistry: CircuitBreakerRegistry,
) {

    @Test
    fun `circuitbreaker configuration test`() {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker("default")
        val circuitBreakerConfig = circuitBreaker.circuitBreakerConfig

        assertThat(circuitBreakerRegistry).isNotNull
        assertThat(circuitBreaker).isNotNull
        assertThat(circuitBreakerConfig).isNotNull
        circuitBreakerConfig.minimumNumberOfCalls shouldBe 30
        circuitBreakerConfig.failureRateThreshold shouldBe 50f
        circuitBreakerConfig.slowCallDurationThreshold.seconds shouldBe 1L
        circuitBreakerConfig.slowCallRateThreshold shouldBe 50f
        circuitBreakerConfig.maxWaitDurationInHalfOpenState.seconds shouldBe 1L
        circuitBreakerConfig.permittedNumberOfCallsInHalfOpenState shouldBe 10
        circuitBreakerConfig.maxWaitDurationInHalfOpenState.seconds shouldBe 1L
    }
}
