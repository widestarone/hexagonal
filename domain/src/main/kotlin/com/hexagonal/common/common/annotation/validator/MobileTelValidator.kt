package com.hexagonal.common.annotation.validator

import com.hexagonal.common.annotation.MobileTel
import com.hexagonal.common.constant.TelValidatorConstant.MOBILE_NUMBER_PATTERN
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class MobileTelValidator : ConstraintValidator<MobileTel, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return true
        }

        return value.matches(MOBILE_NUMBER_PATTERN.toRegex())
    }
}
