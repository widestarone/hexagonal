package com.hexagonal.common.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "이름")
class Name(
    @Schema(name = "기본 이름")
    val defaultName: String,

    @Schema(name = "일본어 이름")
    val nameJa: String? = null,

    @Schema(name = "영어 이름")
    val nameEn: String? = null,

    @Schema(name = "중국어 이름")
    val nameZh: String? = null,

    @Schema(name = "스페인어 이름")
    val nameEs: String? = null,
)
