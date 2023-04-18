package com.hexagonal.common.validator

import com.hexagonal.common.common.annotation.validator.MobileTelValidator
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class MobileTelValidatorTest : AnnotationSpec() {
    private val validator = MobileTelValidator()

    @Test
    fun `휴대폰번호 테스트`() {
        validator.isValid("01012341234", null).shouldBeTrue()
        validator.isValid("01412341234", null).shouldBeFalse()
        validator.isValid("010123412345", null).shouldBeFalse()
        validator.isValid("01612341234", null).shouldBeTrue()
        validator.isValid("0171231234", null).shouldBeTrue()
        validator.isValid("0101231234", null).shouldBeFalse()
        validator.isValid("021231234", null).shouldBeFalse()
        validator.isValid("0212341234", null).shouldBeFalse()
        validator.isValid("0511231234", null).shouldBeFalse()
        validator.isValid("07012341234", null).shouldBeFalse()
    }
}
