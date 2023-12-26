package com.hexagonal.domain.user.application.port.`in`

import com.hexagonal.common.annotation.Tel
import com.hexagonal.domain.user.application.port.out.UserUpdateCommand
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 회원 수정 요청
 */
@Schema(name = "회원 수정")
data class UserModifyCommand(
    @Schema(title = "회원 이메일")
    val email: String,

    @Schema(title = "회원 성")
    val userLastName: String?,

    @Schema(title = "회원 이름")
    val userFirstName: String,

    @Schema(title = "일반 전화번호")
    @field:Tel
    val phoneNumber: String? = null,
) {
    fun toUserUpdatePort(): UserUpdateCommand = UserUpdateCommand(
        email = this.email,
        userLastName = this.userLastName,
        userFirstName = this.userFirstName,
        phoneNumber = this.phoneNumber,
    )
}
