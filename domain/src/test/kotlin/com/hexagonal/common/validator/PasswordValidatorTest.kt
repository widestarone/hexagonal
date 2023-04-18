package com.hexagonal.common.validator

import com.hexagonal.common.common.annotation.validator.PasswordValidator
import io.kotest.core.spec.style.AnnotationSpec

class PasswordValidatorTest : AnnotationSpec() {
    private val validator = PasswordValidator()

    @Test
    fun `영문,숫자,특수문자 모두 포함 8자리 이상`() {
        validator.isValid("123", null)
        validator.isValid("12341234", null)

        validator.isValid("abc", null)
        validator.isValid("abcdabcd", null)

        validator.isValid("!!!", null)
        validator.isValid("!!!@#!@#$", null)

        validator.isValid("abs1234!", null)
    }

    @Test
    fun `영문, 숫자, 특수문자 외의 문자 사용 불가`() {
        validator.isValid("한글추가한글추가123!", null)
    }

    @Test
    fun `빈칸 포함 불가`() {
        validator.isValid("abe1234! 12", null)
    }

    @Test
    fun `같은문자, 숫자 연속 3자 이상 사용 불가`() {
        validator.isValid("aaaaaaaaaabc!2", null)
    }

    @Test
    fun `4자 이상 아이디와 같게 입력 불가`() {
        PasswordValidator.isValidPassword("abcd1234!!", "dabcdd@example.com")
        PasswordValidator.isValidPassword("bdes1234!!", "dabcdd@example.com")
    }
}
