package com.hexagonal.domain.user.domain.service

import com.auth0.jwt.exceptions.TokenExpiredException
import com.hexagonal.common.auth.application.port.`in`.LoginCommand
import com.hexagonal.common.auth.application.port.`in`.RefreshTokenCommand
import com.hexagonal.common.auth.domain.dto.Token
import com.hexagonal.common.auth.helper.jwt.CognitoJwtUtils
import com.hexagonal.common.exception.AuthenticationException
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.common.logging.Log
import com.hexagonal.common.user.application.port.out.cognito.AdminSetUserPasswordCommand
import com.hexagonal.common.user.application.port.out.cognito.AdminUserCreateCommand
import com.hexagonal.common.user.application.port.out.cognito.AwsCognitoPort
import com.hexagonal.common.user.application.port.out.cognito.CognitoLoginCommand
import com.hexagonal.common.user.application.port.out.cognito.CognitoRefreshTokenCommand
import com.hexagonal.common.user.application.port.out.cognito.CognitoWithdrawalCommand
import com.hexagonal.domain.auth.domain.dto.Login
import com.hexagonal.domain.common.dto.ApiResponse
import com.hexagonal.domain.user.application.port.`in`.CompanyUserCommand
import com.hexagonal.domain.user.application.port.`in`.CompanyUserModifyCommand
import com.hexagonal.domain.user.application.port.`in`.StudioUserCommand
import com.hexagonal.domain.user.application.port.`in`.StudioUserModifyCommand
import com.hexagonal.domain.user.application.port.`in`.UserAutoLogInCommand
import com.hexagonal.domain.user.application.port.`in`.UserEmailCommand
import com.hexagonal.domain.user.application.port.`in`.UserPasswordModifyCommand
import com.hexagonal.domain.user.application.port.`in`.UserUseCase
import com.hexagonal.domain.user.application.port.`in`.WithdrawalCommand
import com.hexagonal.domain.user.application.port.`in`.WithdrawalReasonCommand
import com.hexagonal.domain.user.application.port.`in`.validator.UserRequestValidator
import com.hexagonal.domain.user.application.port.out.UserCreateCommand
import com.hexagonal.domain.user.application.port.out.UserPasswordUpdateCommand
import com.hexagonal.domain.user.application.port.out.UserPasswordUpdateResult
import com.hexagonal.domain.user.application.port.out.UserPort
import com.hexagonal.domain.user.application.port.out.WithdrawalReasonCreateCommand
import com.hexagonal.domain.user.constant.CognitoRefreshTokenError
import com.hexagonal.domain.user.constant.SubscribePlanConstant
import com.hexagonal.domain.user.constant.SubscribePlanType
import com.hexagonal.domain.user.constant.UserType
import com.hexagonal.domain.user.domain.dto.CompanyUser
import com.hexagonal.domain.user.domain.dto.StudioSimpleUser
import com.hexagonal.domain.user.domain.dto.StudioUser
import com.hexagonal.domain.user.domain.dto.UserLoginInfo
import com.hexagonal.domain.user.domain.dto.UserSignUp
import com.hexagonal.domain.user.domain.dto.WithdrawalReasonResult
import com.hexagonal.domain.user.domain.helper.AwsCognitoRSAKeyProvider
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException
import java.time.LocalDateTime
import java.util.*

@CacheConfig(cacheNames = ["default"])
@Service
class UserService(
    private val userPort: UserPort,
    private val passwordEncoder: PasswordEncoder,
) : Log, UserUseCase {
    fun signUpUser(request: StudioUserCommand): UserSignUp {
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
            nationCode = request.nationCode,
            email = request.email,
            userType = UserType.STUDIO_USER,
            overFourteen = takeIf { request.overFourteen }?.let { LocalDateTime.now() },
            agreeService = takeIf { request.agreeService }?.let { LocalDateTime.now() },
            agreePersonal = takeIf { request.agreePersonal }?.let { LocalDateTime.now() },
            planCreatedAt = LocalDateTime.now(),
        )

        val userEntity = userPort.save(userCreateCommand)

        val cognitoAuthenticationResult = awsCognitoPort.createUser(
            AdminUserCreateCommand(
                userName = userEntity!!.id.toString(),
                password = password,
                email = request.email,
            ),
        ) ?: run {
            throw ServiceException(errorCode = ErrorCode.INTERNAL_SERVER_ERROR)
        }
        return UserSignUp(
            login = Login(
                userType = userEntity.userType,
                token = cognitoAuthenticationResult.toToken(),
            ),
        )
    }

    fun findUser(userId: Long, currentUser: StudioSimpleUser): ApiResponse<StudioUser> {
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
                data = user.toStudioUser(),
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
    @Cacheable
    override fun findUserOrNull(userId: Long): CompanyUser? = userPort.findById(userId)

    fun updateUser(request: StudioUserModifyCommand, currentUser: StudioSimpleUser): ApiResponse<StudioSimpleUser> {
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
                data = result.toStudioSimpleUser(),
            )
        }
    }

    /**
     * 비밀번호 재설정
     */
    @Transactional
    override fun updateUserPassword(request: UserPasswordModifyCommand): UserPasswordUpdateResult {
        val user = userPort.findValidUserByEmail(request.email) ?: throw ServiceException(ErrorCode.USER_NOT_FOUND)

        val newPassword = passwordEncoder.encode(request.newPassword)
        val result = userPort.updatePassword(
            UserPasswordUpdateCommand(
                userId = user.id,
                newPassword = newPassword,
            ),
        )

        return UserPasswordUpdateResult(
            success = result,
        )
    }
}
