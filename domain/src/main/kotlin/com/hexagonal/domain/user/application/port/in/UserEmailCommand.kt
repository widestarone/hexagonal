package com.hexagonal.domain.user.application.port.`in`

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email

data class UserEmailCommand(
    @Schema(title = "이메일")
    @field:Email
    val email: String,
)
