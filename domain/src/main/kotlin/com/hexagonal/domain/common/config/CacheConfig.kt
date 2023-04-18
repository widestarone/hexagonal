package com.hexagonal.domain.common.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.hexagonal.common.constant.CacheType
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val caches: List<CaffeineCache> = CacheType.values().map { cache ->
            CaffeineCache(
                cache.cacheName,
                Caffeine.newBuilder()
                    .initialCapacity(cache.capacity)
                    .recordStats()
                    .expireAfterAccess(cache.expireTime, TimeUnit.SECONDS)
                    .maximumSize(cache.maximumSize)
                    .build(),
            )
        }

        val cacheManager = SimpleCacheManager()
        cacheManager.setCaches(caches)
        return cacheManager
    }
}
