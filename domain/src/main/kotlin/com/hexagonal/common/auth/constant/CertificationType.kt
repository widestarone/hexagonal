package com.hexagonal.common.auth.constant

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "점유 인증 타입")
enum class CertificationType {
    CLIENT_JOIN, // 회원가입
    CLIENT_FORGOT_PASSWORD, // 비밀번호 찾기
    CLIENT_RESET_PASSWORD, // 비밀번호 재설정
    ADMIN_USER_JOIN, // 어드민 회원가입
}
