package com.hexagonal.domain.common.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 비동기 API (API 응답으론 작업 ID 를 제공하고 메시지 큐를 통해 작업 완료 여부 전달) 기본 응답
 *
 */
@Schema(title = "비동기 API 응답")
data class AsyncApiResponse(

    @Schema(title = "Api 요청 성공 여부")
    var success: Boolean = false,

    @Schema(title = "비동기 작업 ID ")
    var id: Long,

    @Schema(title = "실패 에러코드")
    val errorCode: String? = null,

    @Schema(title = "실패 상세메시지")
    val message: String? = null,
)
