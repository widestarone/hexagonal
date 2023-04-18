package com.hexagonal.domain.common.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "API 응답")
data class ApiResponse<T>(
    @Schema(title = "Api 요청 성공 여부")
    var success: Boolean = false,

    @Schema(title = "데이터")
    var data: T? = null,

    @Schema(title = "실패 에러코드")
    val errorCode: String? = null,

    @Schema(title = "실패 상세메시지")
    val message: String? = null,
)
