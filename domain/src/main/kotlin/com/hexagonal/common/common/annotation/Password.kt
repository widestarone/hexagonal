package com.hexagonal.common.annotation

import com.hexagonal.common.annotation.validator.PasswordValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [PasswordValidator::class])
annotation class Password(
    val message: String = "비밀번호 형식이 잘못되었습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
