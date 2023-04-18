package com.hexagonal.domain.user.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.hexagonal.common.annotation.Tel
import com.hexagonal.domain.common.constant.IndustryType
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email

/**
 * 스튜디오 회원
 */
@Schema(name = "회원")
data class User(
    /**
     * ToDo CompanyCode처럼 별도로 id 관리해야얌. ex. K100 + 00001 -> K10000001
     * 클론세일즈도 같이 반영 필요하므로, 클론세일즈 인터페이스 변경 일정 논의하여 진행 필요
     */
    @JsonIgnore
    @Schema(title = "회원 Id")
    val id: Long,

    @Schema(title = "회원 성")
    val userLastName: String? = null,

    @Schema(title = "회원 이름")
    val userFirstName: String,

    @Schema(title = "일반 전화번호")
    @field:Tel
    val phoneNumber: String? = null,

    @Schema(title = "이메일")
    @field:Email
    val email: String,
)
