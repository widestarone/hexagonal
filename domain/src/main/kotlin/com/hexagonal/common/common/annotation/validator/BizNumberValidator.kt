package com.hexagonal.common.annotation.validator

import com.hexagonal.common.annotation.BizNumber
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 사업자번호 유효성 체크
 * */
class BizNumberValidator : ConstraintValidator<BizNumber, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return when {
            value == null -> false
            value.length != 10 -> false
            else -> true
        }
    }
}
