package com.hexagonal.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val THREAD_NUM = 100

@Configuration
@EnableAsync
class AsyncConfig {

    @Bean(name = ["threadPoolTaskExecutor"])
    fun threadPoolTaskExecutor(): Executor {
        return Executors.newFixedThreadPool(THREAD_NUM)
    }
}
