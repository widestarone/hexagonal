package com.hexagonal.domain.user.application.port.out

import com.hexagonal.domain.user.constant.UserStatus

data class UserStatusUpdateCommand(
    val id: Long,
    val userStatus: UserStatus,
)
