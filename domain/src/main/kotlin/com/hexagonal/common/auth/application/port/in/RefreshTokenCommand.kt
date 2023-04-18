package com.hexagonal.common.auth.application.port.`in`

/**
 * refresh 토큰
 */
data class RefreshTokenCommand(
    val accessToken: String,
    val refreshToken: String,
)
