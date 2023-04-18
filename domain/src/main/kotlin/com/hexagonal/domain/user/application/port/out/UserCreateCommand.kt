package com.hexagonal.domain.user.application.port.out

import com.hexagonal.domain.common.constant.IndustryType
import com.hexagonal.domain.company.adapter.entity.CompanyEntity
import com.hexagonal.domain.user.constant.GenderType
import com.hexagonal.domain.user.constant.SubscribePlanConstant
import com.hexagonal.domain.user.constant.SubscribePlanType
import com.hexagonal.domain.user.constant.UserType
import java.time.LocalDateTime

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
