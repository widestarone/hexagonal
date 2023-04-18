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
import com.hexagonal.domain.user.application.port.`in`.CompanyUserCommand
import com.hexagonal.domain.user.application.port.`in`.StudioUserCommand
import com.hexagonal.domain.user.application.port.`in`.UserEmailCommand
import com.hexagonal.domain.user.application.port.`in`.UserPasswordModifyCommand
import com.hexagonal.domain.user.application.port.`in`.WithdrawalCommand
import com.hexagonal.domain.user.constant.GenderType
import com.hexagonal.domain.user.constant.UserType
import com.hexagonal.domain.user.constant.WithdrawalReasonType
import com.hexagonal.domain.user.domain.dto.CognitoAuthRefreshTokenResult
import com.hexagonal.domain.user.domain.dto.CognitoAuthenticationResult
import com.hexagonal.domain.user.domain.dto.CompanyUser
import com.hexagonal.domain.user.domain.helper.AwsCognitoRSAKeyProvider
import com.hexagonal.domain.util.Randomizer.Companion.makeRandomInstance
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

internal class UserServiceTest {
    private val userRefreshTokenAdapter: UserRefreshTokenAdapter = mockk()
    private val userAdapter: UserAdapter = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val awsCognitoPort: AwsCognitoPort = mockk()
    private val cognitoJwtUtils: CognitoJwtUtils = mockk()
    private val awsCognitoRSAKeyProvider: AwsCognitoRSAKeyProvider = mockk()

    private val userService = UserService(
        userPort = userAdapter,
        passwordEncoder = passwordEncoder,
        cognitoJwtUtils = cognitoJwtUtils,
        awsCognitoRSAKeyProvider = awsCognitoRSAKeyProvider,
        awsCognitoPort = awsCognitoPort,
    )

    @Test
    fun `기업 회원 등록 테스트`() {
        val email = "test@email.com"
        val password = "!abcefdasdf12134"
        val request = makeRandomInstance<CompanyUserCommand>().copy(email = email, password = password)

        val userEntity = UserEntity(
            id = 1,
            userLastName = request.userLastName,
            userFirstName = request.userFirstName,
            email = request.email,
            password = password,
            gender = request.gender,
        )

        val awsCognitoAuth = CognitoAuthenticationResult(
            tokenType = "123",
            accessToken = "1234",
            refreshToken = "1234",
            expiresIn = 1000,
            idToken = "1234",
        )

        every { passwordEncoder.encode(any()) } returns password
        every { userAdapter.save(any()) } returns userEntity
        every { awsCognitoPort.createUser(any()) } returns awsCognitoAuth
        justRun { userRefreshTokenAdapter.save(any()) }
        every { userAdapter.findEntityByEmail(any()) } returns null
        every { userAdapter.findCompanyEntityByCodeOrNull(any()) } returns null

        val result = userService.createCompanyUser(request)

        result.login.token.accessToken shouldBe awsCognitoAuth.accessToken
        result.login.token.refreshToken shouldBe awsCognitoAuth.refreshToken
        result.login.token.grantType shouldBe awsCognitoAuth.tokenType
    }

    @Test
    fun `스튜디오 회원 등록 테스트`() {
        val email = "test@email.com"
        val password = "!abcefdasdf12134"
        val request = makeRandomInstance<StudioUserCommand>().copy(email = email, password = password)

        val userEntity = UserEntity(
            id = 1,
            userLastName = request.userLastName,
            userFirstName = request.userFirstName,
            email = request.email,
            password = password,
        )

        val awsCognitoAuth = CognitoAuthenticationResult(
            tokenType = "123",
            accessToken = "1234",
            refreshToken = "1234",
            expiresIn = 1000,
            idToken = "1234",
        )

        every { passwordEncoder.encode(any()) } returns password
        every { userAdapter.save(any()) } returns userEntity
        every { awsCognitoPort.createUser(any()) } returns awsCognitoAuth
        justRun { userRefreshTokenAdapter.save(any()) }
        every { userAdapter.findEntityByEmail(any()) } returns null
        every { userAdapter.findCompanyEntityByCodeOrNull(any()) } returns null

        val result = userService.signUpStudioUser(request)

        result.login.token.accessToken shouldBe awsCognitoAuth.accessToken
        result.login.token.refreshToken shouldBe awsCognitoAuth.refreshToken
        result.login.token.grantType shouldBe awsCognitoAuth.tokenType
    }

    @Test
    fun `회원 중복 이메일 검증 테스트`() {
        val email = "test@email.com"
        val request = makeRandomInstance<UserEmailCommand>()
            .copy(email = email)

        val userEntity = UserEntity(
            id = 1,
            userLastName = makeRandomInstance(),
            userFirstName = makeRandomInstance(),
            email = request.email,
            password = makeRandomInstance(),
            gender = makeRandomInstance(),
        )

        every { userAdapter.findEntityByEmail(any()) } returns userEntity

        val exception = shouldThrow<ServiceException> {
            userService.checkAvailableUserEmail(request)
        }

        exception.message shouldBe ErrorCode.USER_EXISTS.message
    }

    @Test
    fun `비밀번호 재설정 성공 테스트`() {
        // given
        val userPasswordModifyCommand = UserPasswordModifyCommand(
            email = "test@test.com",
            newPassword = "ASDFasdf1234!@#\$",
        )

        val companyUser = CompanyUser(
            id = 1,
            email = "test@test.com",
            userFirstName = "한강학",
            gender = GenderType.MALE,
            userType = UserType.COMPANY_USER,
            company = makeRandomInstance(),
        )

        val encodedNewPassword = makeRandomInstance<String>()

        // when
        every { passwordEncoder.encode(any()) } returns encodedNewPassword
        every { userAdapter.findValidUserByEmail(any()) } returns companyUser
        every { userAdapter.updatePassword(any()) } returns true
        every { awsCognitoPort.setUserPassword(any()) } returns true

        // then
        val result = userService.updateUserPassword(userPasswordModifyCommand)

        result.success shouldBe true
    }

    @Test
    fun `비밀번호 재설정 db 업데이트 실패 테스트`() {
        // given
        val userPasswordModifyCommand = UserPasswordModifyCommand(
            email = "test@test.com",
            newPassword = "ASDFasdf1234!@#\$",
        )

        val companyUser = CompanyUser(
            id = 1,
            email = "test@test.com",
            userFirstName = "한강학",
            gender = GenderType.MALE,
            userType = UserType.COMPANY_USER,
            company = makeRandomInstance(),
        )

        val encodedNewPassword = makeRandomInstance<String>()

        // when
        every { userAdapter.findValidUserByEmail(any()) } returns companyUser
        every { passwordEncoder.encode(any()) } returns encodedNewPassword
        every { userAdapter.updatePassword(any()) } returns false
        every { awsCognitoPort.setUserPassword(any()) } returns false

        // then
        val result = userService.updateUserPassword(userPasswordModifyCommand)

        result.success shouldBe false
    }

    @Test
    fun `회원 로그인 성공 테스트`() {
        val request = makeRandomInstance<LoginCommand>()
        val user = UserEntity(
            id = makeRandomInstance(),
            password = makeRandomInstance(),
            userFirstName = makeRandomInstance(),
            userLastName = makeRandomInstance(),
            phoneNumber = makeRandomInstance(),
            mobileNumber = makeRandomInstance(),
            email = makeRandomInstance(),
            gender = GenderType.NONE,
            userType = UserType.ASSOCIATE,
        )

        every { userAdapter.findValidEntityByEmail(any()) } returns user
        justRun { userRefreshTokenAdapter.save(any()) }
        every { passwordEncoder.matches(any(), any()) } returns true
        every { awsCognitoPort.loginUser(any()) } returns makeRandomInstance()

        val result = userService.loginUser(request)

        result.login.userType shouldBe user.userType
    }

    @Test
    fun `등록되지 않은 회원 로그인 실패 테스트`() {
        val request = makeRandomInstance<LoginCommand>()

        every { userAdapter.findValidEntityByEmail(any()) } returns null

        val exception = shouldThrow<ServiceException> {
            userService.loginUser(request)
        }

        exception.message shouldBe ErrorCode.USER_NOT_FOUND.message
    }

    @Test
    fun `패스워드가 일치하지 않는 회원 로그인 실패 테스트`() {
        val request = makeRandomInstance<LoginCommand>()
        val user = UserEntity(
            id = makeRandomInstance(),
            password = makeRandomInstance(),
            userFirstName = makeRandomInstance(),
            userLastName = makeRandomInstance(),
            phoneNumber = makeRandomInstance(),
            mobileNumber = makeRandomInstance(),
            email = makeRandomInstance(),
            gender = GenderType.NONE,
        )

        every { userAdapter.findValidEntityByEmail(any()) } returns user
        every { passwordEncoder.matches(any(), any()) } returns false

        val exception = shouldThrow<ServiceException> {
            userService.loginUser(request)
        }

        exception.message shouldBe ErrorCode.BAD_CREDENTIALS_ERROR.message
    }

    @Test
    fun `회원 로그인 토큰 갱신 성공 테스트`() {
        val request = makeRandomInstance<RefreshTokenCommand>()
        val userId = makeRandomInstance<Long>()
        val userEntity = UserEntity(
            id = userId,
            password = makeRandomInstance(),
            userFirstName = makeRandomInstance(),
            userLastName = makeRandomInstance(),
            phoneNumber = makeRandomInstance(),
            mobileNumber = makeRandomInstance(),
            email = makeRandomInstance(),
            gender = GenderType.NONE,
        )

        val newAccessToken = makeRandomInstance<String>()
        val newRefreshToken = makeRandomInstance<String>()

        every { cognitoJwtUtils.getUserIdWithoutVerify(any()) } returns userId
        every { userAdapter.findEntityById(any()) } returns userEntity
        justRun { userRefreshTokenAdapter.updateToken(any(), any()) }
        every { awsCognitoPort.refreshToken(any()) } returns CognitoAuthRefreshTokenResult(
            accessToken = makeRandomInstance(),
            expiresIn = makeRandomInstance(),
            tokenType = "Bearer",
            idToken = "",
        )
        every { awsCognitoPort.loginUser(any()) } returns CognitoAuthenticationResult(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            tokenType = "Bearer",
            idToken = "",
            expiresIn = makeRandomInstance(),
        )

        val result = userService.refreshToken(request)

        result.accessToken shouldBe newAccessToken
        result.refreshToken shouldBe newRefreshToken
    }

    @Test
    fun `일반적인 사유로 인한 탈퇴`() {
        // given
        val withdrawalCommand = WithdrawalCommand(
            reasonType = WithdrawalReasonType.NOT_CONTRACTED_COMPANY,
            reasonDetail = null,
            email = "qwerasdf@klleon.io",
        )
        val companyUser = CompanyUser(
            id = 1,
            userFirstName = "클레온",
            email = "qwerasdf@klleon.io",
            gender = GenderType.MALE,
            userType = UserType.COMPANY_USER,
            company = makeRandomInstance(),
        )

        // when
        every { userAdapter.saveWithdrawalReason(any()) } returns true
        every { userAdapter.withdrawal(any()) } returns true
        every { awsCognitoPort.withdrawal(any()) } returns true

        // then
        val result = userService.withdrawalWithReason(withdrawalCommand, companyUser.id, companyUser.email)

        result shouldBe true
    }

    @Test
    fun `기타 사유로 인한 탈퇴`() {
        // given
        val withdrawalCommand = WithdrawalCommand(
            reasonType = WithdrawalReasonType.ETC,
            reasonDetail = "기타 탈퇴 사유 설명",
            email = "qwerasdf@klleon.io",
        )

        val companyUser = CompanyUser(
            id = 1,
            userFirstName = "클레온",
            email = "qwerasdf@klleon.io",
            gender = GenderType.MALE,
            userType = UserType.COMPANY_USER,
            company = makeRandomInstance(),
        )

        // when
        every { userAdapter.saveWithdrawalReason(any()) } returns true
        every { userAdapter.withdrawal(any()) } returns true
        every { awsCognitoPort.withdrawal(any()) } returns true

        // then
        val result = userService.withdrawalWithReason(withdrawalCommand, companyUser.id, companyUser.email)

        result shouldBe true
    }

    @Test
    fun `유저와 일치하지 않는 이메일로 탈퇴시 실패`() {
        val withdrawalCommand = WithdrawalCommand(
            reasonType = WithdrawalReasonType.ETC,
            reasonDetail = "기타 탈퇴 사유 설명",
            email = "test@klleon.io",
        )

        val companyUser = CompanyUser(
            id = 1,
            userFirstName = "클레온",
            email = "qwerasdf@klleon.io",
            gender = GenderType.MALE,
            userType = UserType.COMPANY_USER,
            company = makeRandomInstance(),
        )

        val exception = shouldThrow<ServiceException> {
            userService.withdrawalWithReason(withdrawalCommand, companyUser.id, companyUser.email)
        }

        exception.message shouldBe ErrorCode.CREDENTIALS_FORBIDDEN_ERROR.message
    }
}
