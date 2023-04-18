package com.hexagonal.common.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "객체화 이미지")
data class ObjectImage(
    @Schema(title = "객체화 이미지 아이디")
    val id: Long,

    @Schema(title = "썸네일 url")
    val imageUrl: String,

    @Schema(title = "x좌표")
    val xLocation: Long,

    @Schema(title = "y좌표")
    val yLocation: Long,

    @Schema(title = "이미지 너비")
    val width: Long,

    @Schema(title = "이미지 높이")
    val height: Long,
)
