package com.hexagonal.domain.common.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class CacheConfig {
//    @Bean
//    fun cacheManager(): CacheManager {
//        val caches: List<CaffeineCache> = CacheType.values().map { cache ->
//            CaffeineCache(
//                cache.cacheName,
//                Caffeine.newBuilder()
//                    .initialCapacity(cache.capacity)
//                    .recordStats()
//                    .expireAfterAccess(cache.expireTime, TimeUnit.SECONDS)
//                    .maximumSize(cache.maximumSize)
//                    .build(),
//            )
//        }
//
//        val cacheManager = SimpleCacheManager()
//        cacheManager.setCaches(caches)
//        return cacheManager
//    }
}
