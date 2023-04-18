package com.hexagonal.common.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema()
data class SuccessResponse<T>(
    @Schema(description = "Is success")
    val success: Boolean,

    @Schema(description = "Response data")
    val data: T,
)
