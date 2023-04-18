package com.hexagonal.domain.user.application.port.`in`

import com.hexagonal.common.annotation.Password
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email

data class UserPasswordModifyCommand(
    @Schema(title = "이메일")
    @field:Email
    val email: String,

    @Schema(title = "새로운 비밀번호")
    @field:Password
    val newPassword: String,
)
