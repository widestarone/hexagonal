package com.hexagonal.domain.user.application.port.`in`.validator

import com.hexagonal.common.annotation.validator.PasswordValidator
import com.hexagonal.common.exception.AuthenticationException
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import org.springframework.stereotype.Component

@Component
object UserRequestValidator {
    fun validateEmail(targetEmail: String, currentUserEmail: String) {
        if (targetEmail != currentUserEmail) {
            throw AuthenticationException(ErrorCode.CREDENTIALS_FORBIDDEN_ERROR)
        }
    }
}
