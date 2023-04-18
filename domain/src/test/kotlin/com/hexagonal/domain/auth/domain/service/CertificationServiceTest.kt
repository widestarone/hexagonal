package com.hexagonal.domain.auth.domain.service

import com.klleon.admin.adminuser.application.out.AdminUserPort
import com.hexagonal.common.auth.adapter.CertificationAdapter
import com.hexagonal.common.auth.adapter.entity.CertificationEntity
import com.hexagonal.common.auth.application.port.`in`.CertificationCommand
import com.hexagonal.common.auth.application.port.`in`.CertificationVerifyCommand
import com.hexagonal.common.auth.constant.CertificationConstant
import com.hexagonal.common.auth.constant.CertificationType
import com.hexagonal.common.auth.domain.dto.Certification
import com.hexagonal.common.auth.domain.dto.ValidationResult
import com.hexagonal.common.auth.domain.helper.AuthCodeGenerator
import com.hexagonal.common.auth.domain.service.CertificationService
import com.hexagonal.common.email.adapter.EmailAdapter
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.domain.user.adapter.UserAdapter
import com.hexagonal.domain.user.domain.dto.CompanyUser
import com.hexagonal.domain.util.Randomizer.Companion.makeRandomInstance
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import java.time.LocalDateTime

internal class CertificationServiceTest : AnnotationSpec() {
    private val certificationAdapter: CertificationAdapter = mockk()
    private val emailAdapter: EmailAdapter = mockk()
    private val userAdapter: UserAdapter = mockk()
    private val adminUserPort: AdminUserPort = mockk()

    private val certificationService = CertificationService(
        certificationPort = certificationAdapter,
        emailPort = emailAdapter,
        userPort = userAdapter,
        adminUserPort = adminUserPort,
    )

    @Test
    fun `회원가입 점유 이메일 인증 요청 테스트`() {
        val email = "test@email.com"
        val request = makeRandomInstance<CertificationCommand>()
            .copy(email = email)
            .copy(certificationType = CertificationType.CLIENT_JOIN)

        val expected = Certification(
            certificationId = 1,
            expiredAt = LocalDateTime.now(),
        )

        every { certificationAdapter.save(any()) } returns expected
        every { emailAdapter.sendCertificateEmail(any()) } returns true
        every { userAdapter.findValidUserByEmail(any()) } returns null

        val result = certificationService.createCertification(request)

        result.certificationId shouldBe 1
        result.expiredAt shouldBe expected.expiredAt
    }

    @Test
    fun `회원가입 점유 이메일 인증 요청 실패 테스트`() {
        val email = "test@email.com"
        val request = makeRandomInstance<CertificationCommand>()
            .copy(email = email)
            .copy(certificationType = CertificationType.CLIENT_JOIN)

        val companyUser = CompanyUser(
            id = 1,
            userFirstName = makeRandomInstance(),
            email = email,
            gender = makeRandomInstance(),
            userType = makeRandomInstance(),
            company = makeRandomInstance(),
        )

        every { emailAdapter.sendCertificateEmail(any()) } returns false
        every { userAdapter.findValidUserByEmail(any()) } returns companyUser

        val exception = shouldThrow<ServiceException> {
            certificationService.createCertification(request)
        }

        exception.message shouldBe ErrorCode.USER_EXISTS.message
    }

    @Test
    fun `점유 이메일 인증 요청 실패 테스트`() {
        val email = "test@email.com"
        val request = makeRandomInstance<CertificationCommand>()
            .copy(email = email)
            .copy(certificationType = CertificationType.CLIENT_JOIN)

        every { emailAdapter.sendCertificateEmail(any()) } returns false
        every { userAdapter.findValidUserByEmail(any()) } returns null

        val exception = shouldThrow<ServiceException> {
            certificationService.createCertification(request)
        }

        exception.message shouldBe ErrorCode.CERTIFICATION_EMAIL_SEND_FAIL.message
    }

    @Test
    fun `비밀번호 변경 점유 이메일 인증 요청 성공 테스트`() {
        val email = "test@email.com"
        val request = makeRandomInstance<CertificationCommand>()
            .copy(email = email)
            .copy(certificationType = CertificationType.CLIENT_RESET_PASSWORD)

        val expected = Certification(
            certificationId = 1,
            expiredAt = LocalDateTime.now(),
        )

        val companyUser = CompanyUser(
            id = 1,
            userFirstName = makeRandomInstance(),
            email = email,
            gender = makeRandomInstance(),
            userType = makeRandomInstance(),
            company = makeRandomInstance(),
        )

        every { certificationAdapter.save(any()) } returns expected
        every { emailAdapter.sendCertificateEmail(any()) } returns true
        every { userAdapter.findValidUserByEmail(any()) } returns companyUser

        val result = certificationService.createCertification(request)

        result.certificationId shouldBe 1
        result.expiredAt shouldBe expected.expiredAt
    }

    @Test
    fun `비밀번호 변경 점유 이메일 인증 요청 실패 테스트`() {
        val email = "test@email.com"
        val request = makeRandomInstance<CertificationCommand>()
            .copy(email = email)
            .copy(certificationType = CertificationType.CLIENT_RESET_PASSWORD)

        every { emailAdapter.sendCertificateEmail(any()) } returns false
        every { userAdapter.findValidUserByEmail(any()) } returns null

        val exception = shouldThrow<ServiceException> {
            certificationService.createCertification(request)
        }

        exception.message shouldBe ErrorCode.USER_NOT_FOUND.message
    }

    @Test
    fun `점유 이메일 인증 확인 테스트`() {
        val email = "test@email.com"
        val certificationCode = AuthCodeGenerator.generateCode()
        val request = makeRandomInstance<CertificationVerifyCommand>()
            .copy(email = email, certificationCode = certificationCode)

        val expected = CertificationEntity(
            id = 1,
            certificationType = CertificationType.CLIENT_JOIN,
            email = email,
            certificationCode = certificationCode,
            expiredAt = LocalDateTime.now()
                .plusMinutes(CertificationConstant.EXPIRE_DURATION),
        )
        every { certificationAdapter.findEntityById(any()) } returns expected
        justRun { certificationAdapter.confirmed(any()) }

        val result = certificationService.verifyCertification(request)

        result shouldBe ValidationResult(
            validation = true,
        )
    }
}
