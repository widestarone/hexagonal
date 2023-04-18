package com.hexagonal.common.auth.application.port.`in`

import com.hexagonal.common.auth.domain.dto.Certification
import com.hexagonal.common.auth.domain.dto.ValidationResult

interface CertificationUseCase {
    fun createCertification(request: CertificationCommand): Certification

    fun verifyCertification(request: CertificationVerifyCommand): ValidationResult
}
