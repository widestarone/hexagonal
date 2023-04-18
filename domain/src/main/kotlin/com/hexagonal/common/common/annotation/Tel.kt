package com.hexagonal.common.annotation

import com.hexagonal.common.annotation.validator.TelValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [TelValidator::class])
annotation class Tel(
    val message: String = "전화번호 형식이 잘못되었습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
