package com.hexagonal.domain.auth.domain.dto

import com.hexagonal.common.auth.domain.dto.Token
import com.hexagonal.domain.user.constant.UserType
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 로그인 response
 */
data class Login(

    @Schema(title = "사용자 타입(준회원, 정회원, 기업 관리자, 마스터 관리자")
    val userType: UserType,

    @Schema(title = "인증 토큰")
    val token: Token,
)
