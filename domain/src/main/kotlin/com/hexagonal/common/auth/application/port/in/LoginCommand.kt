package com.hexagonal.common.auth.application.port.`in`

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 로그인 request
 */
data class LoginCommand(
    @Schema(title = "이메일")
    val email: String,
    @Schema(title = "패스워드")
    val password: String,
)
