package com.hexagonal.domain.user.application.port.out

data class UserPasswordUpdateCommand(
    val userId: Long,
    val newPassword: String,
)
