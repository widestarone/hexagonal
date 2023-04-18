package com.hexagonal.domain.user.application.port.out

import com.hexagonal.domain.common.constant.IndustryType

/**
 * 회원 수정 요청
 */
data class UserUpdateCommand(
    val email: String,
    val userLastName: String? = null,
    val userFirstName: String,
    val phoneNumber: String? = null,
    val mobileNumber: String? = null,
)
