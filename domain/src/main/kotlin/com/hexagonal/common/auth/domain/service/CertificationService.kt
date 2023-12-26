package com.hexagonal.common.auth.domain.service

import com.hexagonal.common.auth.application.port.`in`.CertificationCommand
import com.hexagonal.common.auth.application.port.`in`.CertificationUseCase
import com.hexagonal.common.auth.application.port.`in`.CertificationVerifyCommand
import com.hexagonal.common.auth.constant.CertificationConstant.ADMIN_CERTIFICATE_EMAIL_TITLE
import com.hexagonal.common.auth.constant.CertificationConstant.CERTIFICATE_EMAIL_TEMPLATE
import com.hexagonal.common.auth.constant.CertificationConstant.CERTIFICATE_EMAIL_TITLE
import com.hexagonal.common.auth.constant.CertificationType
import com.hexagonal.common.auth.domain.dto.Certification
import com.hexagonal.common.auth.domain.dto.ValidationResult
import com.hexagonal.common.auth.domain.helper.AuthCodeGenerator
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.domain.user.application.port.out.UserPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CertificationService(
    private val userPort: UserPort,
) : CertificationUseCase {
    @Transactional
    override fun createCertification(request: CertificationCommand): Certification {
        // 이메일 유효성 검증
        checkValidUserRequest(request)

        val certificationCode = AuthCodeGenerator.generateCode()
        throw ServiceException(ErrorCode.CERTIFICATION_EMAIL_SEND_FAIL)
    }

    @Transactional
    override fun verifyCertification(
        request: CertificationVerifyCommand,
    ): ValidationResult {
        return ValidationResult(
            validation = true,
        )
    }

    /**
     *     CLIENT_JOIN, // 존재하지 않는 이메일 확인
     *     CLIENT_FORGOT_PASSWORD, // 존재하는 이메일 확인
     *     CLIENT_RESET_PASSWORD, // 존재하는 이메일 확인
     *     ADMIN_USER_JOIN // 존재하지 않는 이메일 확인
     */
    private fun checkValidUserRequest(request: CertificationCommand) {
        when (request.certificationType) {
            CertificationType.CLIENT_JOIN -> takeIf { userPort.findEntityByEmail(request.email) == null }
                ?: throw ServiceException(ErrorCode.USER_EXISTS)

            CertificationType.CLIENT_FORGOT_PASSWORD -> takeIf { userPort.findEntityByEmail(request.email) != null }
                ?: throw ServiceException(ErrorCode.USER_NOT_FOUND)

            CertificationType.CLIENT_RESET_PASSWORD -> takeIf { userPort.findEntityByEmail(request.email) != null }
                ?: throw ServiceException(ErrorCode.USER_NOT_FOUND)

            CertificationType.ADMIN_USER_JOIN -> TODO()
        }
    }

    private fun findEmailTitle(certificationType: CertificationType): String {
        return when (certificationType) {
            CertificationType.ADMIN_USER_JOIN -> ADMIN_CERTIFICATE_EMAIL_TITLE
            else -> CERTIFICATE_EMAIL_TITLE
        }
    }
}
