package com.hexagonal.common.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.hexagonal.common.exception.ErrorResponse
import java.time.LocalDateTime

class BaseResponse<T>(
    val success: Boolean,
    val data: T,
    val error: ErrorResponse? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
