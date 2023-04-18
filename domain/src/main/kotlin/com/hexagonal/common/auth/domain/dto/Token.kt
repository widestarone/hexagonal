package com.hexagonal.common.auth.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

data class Token(
    @Schema(title = "권한 부여 유형")
    val grantType: String,

    @Schema(title = "접근 토큰")
    val accessToken: String,

    @Schema(title = "갱신 토큰")
    val refreshToken: String? = null,

    @Schema(title = "접큰 토큰 유효 시간(초)")
    val accessTokenExpiresIn: Int,
)
