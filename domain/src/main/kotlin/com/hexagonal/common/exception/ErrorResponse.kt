package com.hexagonal.common.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "에러 메시지")
data class ErrorResponse(

    @Schema(title = "에러 코드")
    val code: String,
    val status: String?,
    val message: String? = null,
    var path: String? = null,
    @Schema(title = "validation 에러 코드 (422인 경우에만 노출)")
    val invalidParams: List<ConstraintErrorMessage>? = null,
)
