package com.hexagonal.domain.user.application.port.`in`

import com.hexagonal.common.annotation.Password
import com.hexagonal.common.annotation.Tel
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email

/**
 * 회원 가입 요청
 */
@Schema(name = "회원 가입")
data class UserCommand(
    @Schema(title = "회원 이름")
    val userLastName: String,

    @Schema(title = "회원 성")
    val userFirstName: String,

    @Schema(title = "비밀번호")
    @field:Password
    val password: String,

    @Schema(title = "이메일")
    @field:Email
    val email: String,

    @Schema(title = "전화번호")
    @field:Tel
    val phoneNumber: String? = null,
)
