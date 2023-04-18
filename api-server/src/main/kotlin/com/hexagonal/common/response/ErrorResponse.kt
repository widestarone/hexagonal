package com.hexagonal.common.response

import io.swagger.v3.oas.annotations.media.Schema

data class ErrorResponse(
    @Schema(description = "성공 여부")
    val success: Boolean = false,

    @Schema(title = "실패 에러코드")
    val errorCode: String? = null,

    @Schema(title = "실패 상세메시지")
    val message: String? = null,
)
