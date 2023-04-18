package com.hexagonal.common.annotation.validator

import com.hexagonal.common.annotation.Password
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 패스워드 Validator
 * */
class PasswordValidator : ConstraintValidator<Password, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val pattern = Regex("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#\$%^&*()]).{8,20}$")
        val repeatPattern = Regex("(\\w)\\1\\1\\1")

        return when {
            value == null -> false
            value.contains(" ") -> false
            value.length !in 8..20 -> false
            !value.matches(pattern) -> false
            value.contains(repeatPattern) -> false
            else -> true
        }
    }

    companion object {
        /**
         * password 패스워드
         * email 패스워드에 이메일 주소가 포함되어 있는 지 확인을 위해 이용
         */
        fun isValidPassword(password: String, email: String, birthStr: String? = null): Boolean {
            val id = email.takeWhile { it != '@' }
            // password에 email이 포함되어 있으면 등록 불가
            for (i in 0..id.length - 4) {
                val compareStr = id.substring(i, i + 4)
                if (compareStr in password) return false
            }

            // password에 생년월일이 포함되어 있으면 등록 불가
            if (birthStr != null) {
                for (i in 0..birthStr.length - 4) {
                    val compareStr = id.substring(i, i + 4)
                    if (compareStr in birthStr) return false
                }
            }
            return true
        }
    }
}
