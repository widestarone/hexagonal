package com.hexagonal.domain.user.application.port.`in`.validator

import com.hexagonal.common.annotation.validator.PasswordValidator
import com.hexagonal.common.exception.AuthenticationException
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.domain.user.application.port.`in`.CompanyUserCommand
import org.springframework.stereotype.Component

@Component
object UserRequestValidator {
    fun validate(request: CompanyUserCommand) {
        takeUnless { PasswordValidator.isValidPassword(request.password, request.email) }
            ?.let { throw ServiceException(ErrorCode.INVALID_USER_PASSWORD) }
    }

    fun validateEmail(targetEmail: String, currentUserEmail: String) {
        if (targetEmail != currentUserEmail) {
            throw AuthenticationException(ErrorCode.CREDENTIALS_FORBIDDEN_ERROR)
        }
    }
}
