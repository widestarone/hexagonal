package com.hexagonal.common.constant

enum class CacheType(
    val cacheName: String,
    val capacity: Int,
    val maximumSize: Long,
    val expireTime: Long,
) {
    DEFAULT("default", 100, 500L, 60L),
    USER("user", 100, 1000L, 1L),
}
