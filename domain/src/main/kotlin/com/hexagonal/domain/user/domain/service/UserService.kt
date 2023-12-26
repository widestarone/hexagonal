package com.hexagonal.domain.user.domain.service

import com.hexagonal.common.auth.application.port.`in`.LoginCommand
import com.hexagonal.common.auth.application.port.`in`.RefreshTokenCommand
import com.hexagonal.common.auth.domain.dto.Token
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.common.logging.Log
import com.hexagonal.domain.auth.domain.dto.Login
import com.hexagonal.domain.common.dto.ApiResponse
import com.hexagonal.domain.user.application.port.`in`.*
import com.hexagonal.domain.user.application.port.out.UserCreateCommand
import com.hexagonal.domain.user.application.port.out.UserPasswordUpdateCommand
import com.hexagonal.domain.user.application.port.out.UserPasswordUpdateResult
import com.hexagonal.domain.user.application.port.out.UserPort
import com.hexagonal.domain.user.constant.UserType
import com.hexagonal.domain.user.domain.dto.User
import com.hexagonal.domain.user.domain.dto.UserLoginInfo
import com.hexagonal.domain.user.domain.dto.UserSignUp
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@CacheConfig(cacheNames = ["default"])
@Service
class UserService(
    private val userPort: UserPort,
    private val passwordEncoder: PasswordEncoder,
) : Log, UserUseCase {
    fun signUpUser(request: UserCommand): UserSignUp {
        userPort.findEntityByEmail(request.email)?.let {
            val errorCode = when (it.isWithdrawal()) {
                true -> ErrorCode.WITHDRAWAL_USER
                else -> ErrorCode.USER_EXISTS
            }
            throw ServiceException(errorCode = errorCode)
        }

        val password = passwordEncoder.encode(request.password)

        val userCreateCommand = UserCreateCommand(
            password = password,
            userLastName = request.userLastName,
            userFirstName = request.userFirstName,
            phoneNumber = request.phoneNumber,
            email = request.email,
            userType = UserType.STUDIO_USER,
        )

        val userEntity = userPort.save(userCreateCommand)
        return UserSignUp(
            login = Login(
                userType = userEntity?.userType!!,
                token = Token("","","",123),
            ),
        )
    }

    fun findUser(userId: Long, currentUser: User): ApiResponse<User> {
        takeUnless { userId == currentUser.id }?.run {
            val errorCode = ErrorCode.USER_NOT_MATCHED
            return ApiResponse(
                errorCode = errorCode.code,
                message = errorCode.message,
            )
        }

        return when (val user = userPort.findEntityById(userId)) {
            null -> {
                val errorCode = ErrorCode.USER_NOT_FOUND
                ApiResponse(
                    errorCode = errorCode.code,
                    message = errorCode.message,
                )
            }

            else -> ApiResponse(
                success = true,
                data = user.toUser(),
            )
        }
    }

    /**
     * 회원 가입시 중복 이메일 체크
     */
    @Transactional(readOnly = true)
    override fun checkAvailableUserEmail(request: UserEmailCommand): Boolean {
        userPort.findEntityByEmail(request.email)?.let {
            if (it.isWithdrawal()) {
                throw ServiceException(ErrorCode.WITHDRAWAL_USER)
            }
            throw ServiceException(ErrorCode.USER_EXISTS)
        }
        return true
    }

    /**
     * 회원 조회
     */
//    @Cacheable
//    override fun findUserOrNull(userId: Long): User? = userPort.findEntityById(userId)?.toUser()

    fun updateUser(request: UserModifyCommand, currentUser: User): ApiResponse<User> {
        takeUnless { request.email == currentUser.email }?.run {
            val errorCode = ErrorCode.USER_NOT_MATCHED
            return ApiResponse(
                errorCode = errorCode.code,
                message = errorCode.message,
            )
        }

        return when (val result = userPort.update(request.toUserUpdatePort())) {
            null -> {
                val errorCode = ErrorCode.USER_NOT_FOUND
                ApiResponse(
                    errorCode = errorCode.code,
                    message = errorCode.message,
                )
            }

            else -> ApiResponse(
                success = true,
                data = result.toUser(),
            )
        }
    }

    /**
     * 비밀번호 재설정
     */
    @Transactional
    override fun updateUserPassword(request: UserPasswordModifyCommand): UserPasswordUpdateResult {
        val user = userPort.findValidEntityByEmail(request.email) ?: throw ServiceException(ErrorCode.USER_NOT_FOUND)

        val newPassword = passwordEncoder.encode(request.newPassword)
        val result = userPort.updatePassword(
            UserPasswordUpdateCommand(
                userId = user.id!!,
                newPassword = newPassword,
            ),
        )

        return UserPasswordUpdateResult(
            success = result,
        )
    }

    override fun loginUser(request: LoginCommand): UserLoginInfo {
        TODO("Not yet implemented")
    }

    override fun refreshToken(refreshTokenCommand: RefreshTokenCommand): Token {
        TODO("Not yet implemented")
    }

    override fun autoLoginUser(request: UserAutoLogInCommand): Boolean {
        TODO("Not yet implemented")
    }
}
