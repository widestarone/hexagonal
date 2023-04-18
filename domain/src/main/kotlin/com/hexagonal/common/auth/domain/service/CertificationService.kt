package com.hexagonal.common.auth.domain.service

import com.klleon.admin.adminuser.application.out.AdminUserPort
import com.hexagonal.common.auth.application.port.`in`.CertificationCommand
import com.hexagonal.common.auth.application.port.`in`.CertificationUseCase
import com.hexagonal.common.auth.application.port.`in`.CertificationVerifyCommand
import com.hexagonal.common.auth.application.port.out.CertificationPort
import com.hexagonal.common.auth.constant.CertificationConstant.ADMIN_CERTIFICATE_EMAIL_TITLE
import com.hexagonal.common.auth.constant.CertificationConstant.CERTIFICATE_EMAIL_TEMPLATE
import com.hexagonal.common.auth.constant.CertificationConstant.CERTIFICATE_EMAIL_TITLE
import com.hexagonal.common.auth.constant.CertificationType
import com.hexagonal.common.auth.domain.dto.Certification
import com.hexagonal.common.auth.domain.dto.ValidationResult
import com.hexagonal.common.auth.domain.helper.AuthCodeGenerator
import com.hexagonal.common.util.date.DateUtil.isPast
import com.hexagonal.common.email.application.port.out.CertificateEmailSendCommand
import com.hexagonal.common.email.application.port.out.EmailPort
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.domain.user.application.port.out.UserPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CertificationService(
    private val certificationPort: CertificationPort,
    private val emailPort: EmailPort,
    private val userPort: UserPort,
    private val adminUserPort: AdminUserPort,
) : CertificationUseCase {
    @Transactional
    override fun createCertification(request: CertificationCommand): Certification {
        // 이메일 유효성 검증
        checkValidUserRequest(request)

        val certificationCode = AuthCodeGenerator.generateCode()

        val emailRequest = CertificateEmailSendCommand(
            email = request.email,
            certificationCode = certificationCode,
            template = CERTIFICATE_EMAIL_TEMPLATE,
            title = findEmailTitle(request.certificationType),
        )

        return when (emailPort.sendCertificateEmail(emailRequest)) {
            true -> certificationPort.save(request.toCertificationCreatePort(certificationCode))
            else -> throw ServiceException(ErrorCode.CERTIFICATION_EMAIL_SEND_FAIL)
        }
    }

    @Transactional
    override fun verifyCertification(
        request: CertificationVerifyCommand,
    ): ValidationResult {
        val certificationEntity = certificationPort.findEntityById(request.certificationId)
            ?: throw ServiceException(ErrorCode.CERTIFICATION_NOT_FOUND)

        when {
            certificationEntity.confirmedAt != null -> throw ServiceException(ErrorCode.CERTIFICATION_DUPLICATE)
            certificationEntity.expiredAt.isPast() -> throw ServiceException(ErrorCode.CERTIFICATION_EXPIRED)
            certificationEntity.email != request.email -> throw ServiceException(ErrorCode.CERTIFICATION_NOT_FOUND)
            certificationEntity.certificationCode != request.certificationCode
            -> throw ServiceException(ErrorCode.CERTIFICATION_MIS_MATCH)
        }

        certificationPort.confirmed(certificationEntity)

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
            CertificationType.CLIENT_JOIN -> takeIf { userPort.findValidUserByEmail(request.email) == null }
                ?: throw ServiceException(ErrorCode.USER_EXISTS)

            CertificationType.CLIENT_FORGOT_PASSWORD -> takeIf { userPort.findValidUserByEmail(request.email) != null }
                ?: throw ServiceException(ErrorCode.USER_NOT_FOUND)

            CertificationType.CLIENT_RESET_PASSWORD -> takeIf { userPort.findValidUserByEmail(request.email) != null }
                ?: throw ServiceException(ErrorCode.USER_NOT_FOUND)

            CertificationType.ADMIN_USER_JOIN -> takeIf { adminUserPort.findAdminUserByEmail(request.email) == null }
                ?: throw ServiceException(ErrorCode.ADMIN_USER_EXISTS)
        }
    }

    private fun findEmailTitle(certificationType: CertificationType): String {
        return when (certificationType) {
            CertificationType.ADMIN_USER_JOIN -> ADMIN_CERTIFICATE_EMAIL_TITLE
            else -> CERTIFICATE_EMAIL_TITLE
        }
    }
}
