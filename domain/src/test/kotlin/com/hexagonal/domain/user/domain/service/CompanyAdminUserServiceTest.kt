package com.hexagonal.domain.user.domain.service

import com.hexagonal.common.auth.adapter.UserRefreshTokenAdapter
import com.hexagonal.common.auth.application.port.`in`.LoginCommand
import com.hexagonal.common.auth.application.port.`in`.RefreshTokenCommand
import com.hexagonal.common.auth.helper.jwt.CognitoJwtUtils
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.common.user.application.port.out.cognito.AwsCognitoPort
import com.hexagonal.domain.user.adapter.UserAdapter
import com.hexagonal.domain.user.adapter.entity.UserEntity
import com.hexagonal.domain.user.application.port.`in`.UserAutoLogInCommand
import com.hexagonal.domain.user.constant.GenderType
import com.hexagonal.domain.user.constant.UserType
import com.hexagonal.domain.user.domain.dto.CognitoAuthRefreshTokenResult
import com.hexagonal.domain.user.domain.dto.CognitoAuthenticationResult
import com.hexagonal.domain.user.domain.helper.AwsCognitoRSAKeyProvider
import com.hexagonal.domain.util.Randomizer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class CompanyAdminUserServiceTest {
    private val userRefreshTokenAdapter: UserRefreshTokenAdapter = mockk()
    private val userAdapter: UserAdapter = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val awsCognitoPort: AwsCognitoPort = mockk()
    private val cognitoJwtUtils: CognitoJwtUtils = mockk()
    private val awsCognitoRSAKeyProvider: AwsCognitoRSAKeyProvider = mockk()

    private val companyAdminUserService = CompanyAdminUserService(
        userPort = userAdapter,
        passwordEncoder = passwordEncoder,
        cognitoJwtUtils = cognitoJwtUtils,
        awsCognitoRSAKeyProvider = awsCognitoRSAKeyProvider,
        awsCognitoPort = awsCognitoPort,
    )

    @Test
    fun `기업 어드민 앱 회원 로그인 성공 테스트`() {
        val request = Randomizer.makeRandomInstance<LoginCommand>()
        val user = UserEntity(
            id = Randomizer.makeRandomInstance(),
            password = Randomizer.makeRandomInstance(),
            userFirstName = Randomizer.makeRandomInstance(),
            userLastName = Randomizer.makeRandomInstance(),
            phoneNumber = Randomizer.makeRandomInstance(),
            mobileNumber = Randomizer.makeRandomInstance(),
            email = Randomizer.makeRandomInstance(),
            gender = GenderType.NONE,
            userType = UserType.COMPANY_ADMIN,
        )

        every { userAdapter.findValidEntityByEmail(any()) } returns user
        justRun { userRefreshTokenAdapter.save(any()) }
        every { passwordEncoder.matches(any(), any()) } returns true
        every { awsCognitoPort.loginUser(any()) } returns Randomizer.makeRandomInstance()

        val result = companyAdminUserService.loginCompanyAdminUser(request)

        result.login.userType shouldBe UserType.COMPANY_ADMIN
    }

    @Test
    fun `기업 어드민이 아닌 회원 로그인 실패 테스트`() {
        val request = Randomizer.makeRandomInstance<LoginCommand>()
        val user = UserEntity(
            id = Randomizer.makeRandomInstance(),
            password = Randomizer.makeRandomInstance(),
            userFirstName = Randomizer.makeRandomInstance(),
            userLastName = Randomizer.makeRandomInstance(),
            phoneNumber = Randomizer.makeRandomInstance(),
            mobileNumber = Randomizer.makeRandomInstance(),
            email = Randomizer.makeRandomInstance(),
            gender = GenderType.NONE,
            userType = UserType.COMPANY_USER,
        )

        every { userAdapter.findValidEntityByEmail(any()) } returns user

        val exception = shouldThrow<ServiceException> {
            companyAdminUserService.loginCompanyAdminUser(request)
        }

        exception.message shouldBe ErrorCode.FORBIDDEN_USER_ERROR.message
    }

    @Test
    fun `기업 어드민 앱 회원 로그인 토큰 갱신 성공 테스트`() {
        val request = Randomizer.makeRandomInstance<RefreshTokenCommand>()
        val userId = Randomizer.makeRandomInstance<Long>()
        val userEntity = UserEntity(
            id = userId,
            password = Randomizer.makeRandomInstance(),
            userFirstName = Randomizer.makeRandomInstance(),
            userLastName = Randomizer.makeRandomInstance(),
            phoneNumber = Randomizer.makeRandomInstance(),
            mobileNumber = Randomizer.makeRandomInstance(),
            email = Randomizer.makeRandomInstance(),
            gender = GenderType.NONE,
            userType = UserType.COMPANY_ADMIN,
        )

        val newAccessToken = Randomizer.makeRandomInstance<String>()
        val newRefreshToken = Randomizer.makeRandomInstance<String>()

        every { cognitoJwtUtils.getUserIdWithoutVerify(any()) } returns userId
        every { userAdapter.findEntityById(any()) } returns userEntity
        justRun { userRefreshTokenAdapter.updateToken(any(), any()) }
        every { awsCognitoPort.refreshToken(any()) } returns CognitoAuthRefreshTokenResult(
            accessToken = Randomizer.makeRandomInstance(),
            expiresIn = Randomizer.makeRandomInstance(),
            tokenType = "Bearer",
            idToken = "",
        )
        every { awsCognitoPort.loginUser(any()) } returns CognitoAuthenticationResult(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            tokenType = "Bearer",
            idToken = "",
            expiresIn = Randomizer.makeRandomInstance(),
        )

        val result = companyAdminUserService.refreshCompanyAdminUserToken(request)

        result.accessToken shouldBe newAccessToken
        result.refreshToken shouldBe newRefreshToken
    }

    @Test
    fun `기업 어드민이 아닌 회원 로그인 토큰 갱신 실패 테스트`() {
        val request = Randomizer.makeRandomInstance<RefreshTokenCommand>()
        val userId = Randomizer.makeRandomInstance<Long>()
        val userEntity = UserEntity(
            id = userId,
            password = Randomizer.makeRandomInstance(),
            userFirstName = Randomizer.makeRandomInstance(),
            userLastName = Randomizer.makeRandomInstance(),
            phoneNumber = Randomizer.makeRandomInstance(),
            mobileNumber = Randomizer.makeRandomInstance(),
            email = Randomizer.makeRandomInstance(),
            gender = GenderType.NONE,
            userType = UserType.COMPANY_USER,
        )

        every { cognitoJwtUtils.getUserIdWithoutVerify(any()) } returns userId
        every { userAdapter.findEntityById(any()) } returns userEntity

        val exception = shouldThrow<ServiceException> {
            companyAdminUserService.refreshCompanyAdminUserToken(request)
        }

        exception.message shouldBe ErrorCode.FORBIDDEN_USER_ERROR.message
    }

    @Test
    fun `기업 어드민 앱 회원 자동 로그인 성공 테스트`() {
        val request = Randomizer.makeRandomInstance<UserAutoLogInCommand>()
        val userId = Randomizer.makeRandomInstance<Long>()
        val userEntity = UserEntity(
            id = userId,
            password = Randomizer.makeRandomInstance(),
            userFirstName = Randomizer.makeRandomInstance(),
            userLastName = Randomizer.makeRandomInstance(),
            phoneNumber = Randomizer.makeRandomInstance(),
            mobileNumber = Randomizer.makeRandomInstance(),
            email = Randomizer.makeRandomInstance(),
            gender = GenderType.NONE,
            userType = UserType.COMPANY_ADMIN,
        )

        every { cognitoJwtUtils.getUserIdWithoutVerify(any()) } returns userId
        every { userAdapter.findEntityById(any()) } returns userEntity
        every { cognitoJwtUtils.verifyToken(any(), any()) } returns true

        val result = companyAdminUserService.autoLoginCompanyAdminUser(request)

        result shouldBe true
    }

    @Test
    fun `기업 어드민이 아닌 회원 자동 로그인 실패 테스트`() {
        val request = Randomizer.makeRandomInstance<UserAutoLogInCommand>()
        val userId = Randomizer.makeRandomInstance<Long>()
        val userEntity = UserEntity(
            id = userId,
            password = Randomizer.makeRandomInstance(),
            userFirstName = Randomizer.makeRandomInstance(),
            userLastName = Randomizer.makeRandomInstance(),
            phoneNumber = Randomizer.makeRandomInstance(),
            mobileNumber = Randomizer.makeRandomInstance(),
            email = Randomizer.makeRandomInstance(),
            gender = GenderType.NONE,
            userType = UserType.COMPANY_USER,
        )

        every { cognitoJwtUtils.getUserIdWithoutVerify(any()) } returns userId
        every { userAdapter.findEntityById(any()) } returns userEntity

        val exception = shouldThrow<ServiceException> {
            companyAdminUserService.autoLoginCompanyAdminUser(request)
        }

        exception.message shouldBe ErrorCode.FORBIDDEN_USER_ERROR.message
    }
}
