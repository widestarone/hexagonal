package com.hexagonal.common.annotation

import com.hexagonal.common.annotation.validator.BizNumberValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [BizNumberValidator::class])
annotation class BizNumber(
    val message: String = "사업자번호 형식이 잘못되었습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
