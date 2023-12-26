package com.hexagonal.common.auth.application.port.`in`

import com.hexagonal.common.auth.constant.CertificationType
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.SchemaProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(title = "점유인증생성 Request")
data class CertificationCommand(
    @field:NotBlank
    @field:Email
    @Schema(title = "이메일")
    val email: String,

    @field:NotNull
    @SchemaProperty(schema = Schema(implementation = CertificationType::class))
    val certificationType: CertificationType,
) {
//    fun toCertificationCreatePort(code: String): CertificationCreateCommand = CertificationCreateCommand(
//        email = this.email,
//        certificationType = this.certificationType,
//        certificationCode = code,
//    )
}
