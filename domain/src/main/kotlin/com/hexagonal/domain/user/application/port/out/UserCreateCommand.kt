package com.hexagonal.domain.user.application.port.out

import com.hexagonal.domain.user.constant.UserType

/**
 * 회원 data port
 */
data class UserCreateCommand(
    val userLastName: String? = null,
    val userFirstName: String,
    val password: String,
    val phoneNumber: String? = null,
    val mobileNumber: String? = null,
    val email: String,
    val userType: UserType = UserType.ASSOCIATE,
)
