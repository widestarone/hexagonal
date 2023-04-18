package com.hexagonal.domain.user.domain.dto

import com.hexagonal.domain.auth.domain.dto.Login

data class UserLoginInfo(
    val login: Login,
)
