package com.hexagonal.common.annotation.validator

import com.hexagonal.common.annotation.Tel
import com.hexagonal.common.constant.TelValidatorConstant.LAND_LINE_NUMBER_PATTERN
import com.hexagonal.common.constant.TelValidatorConstant.MOBILE_NUMBER_PATTERN
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TelValidator : ConstraintValidator<Tel, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return true
        }

        return value.matches(MOBILE_NUMBER_PATTERN.toRegex()) ||
            value.matches(LAND_LINE_NUMBER_PATTERN.toRegex())
    }
}
