package com.hexagonal.domain.user.application.port.`in`

import com.hexagonal.common.auth.application.port.`in`.LoginCommand
import com.hexagonal.common.auth.application.port.`in`.RefreshTokenCommand
import com.hexagonal.common.auth.domain.dto.Token
import com.hexagonal.domain.common.dto.ApiResponse
import com.hexagonal.domain.user.application.port.out.UserPasswordUpdateResult
import com.hexagonal.domain.user.domain.dto.User
import com.hexagonal.domain.user.domain.dto.UserLoginInfo
import com.hexagonal.domain.user.domain.dto.UserSignUp

interface UserUseCase {

    fun checkAvailableUserEmail(request: UserEmailCommand): Boolean

    fun updateUserPassword(request: UserPasswordModifyCommand): UserPasswordUpdateResult

    fun loginUser(request: LoginCommand): UserLoginInfo

    fun refreshToken(refreshTokenCommand: RefreshTokenCommand): Token

    fun autoLoginUser(request: UserAutoLogInCommand): Boolean
}
