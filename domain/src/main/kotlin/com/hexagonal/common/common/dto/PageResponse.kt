package com.hexagonal.common.dto
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "페이지 정보 응답")
class PageResponse<T>(data: List<T>, page: Int, pageSize: Int, next: Boolean) {
    @Schema(title = "데이터")
    val data: List<T> = data

    @Schema(title = "현재 페이지")
    val currentPage: Int = page

    @Schema(title = "조회 가능한 최대 페이지")
    val pageTotalSize: Int = pageSize

    @Schema(title = "다음 페이지 조회 가능 여부")
    val next: Boolean = next
}
