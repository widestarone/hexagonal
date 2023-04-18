package com.hexagonal.common.auth.application.port.out

/**
 * refresh 토큰
 */
data class UserRefreshTokenCreateCommand(
    val key: String,
    val value: String,
)
