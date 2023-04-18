package com.hexagonal.common.auth.application.port.`in`

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@Schema(title = "점유인증확인 Request")
data class CertificationVerifyCommand(
    @field:Positive
    @Schema(title = "아이디")
    val certificationId: Long,

    @field:NotBlank
    @field:Email
    @Schema(title = "이메일")
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, max = 6)
    @Schema(title = "6자리 인증 코드")
    val certificationCode: String,
)
