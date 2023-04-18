package com.hexagonal.common.auth.domain.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "점유인증 Response")
data class Certification(
    val certificationId: Long,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val expiredAt: LocalDateTime? = null,
)
