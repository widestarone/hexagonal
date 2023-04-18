package com.hexagonal.common.annotation

import io.swagger.v3.oas.annotations.Hidden

/**
 * 로그인 사용자 정보 확인
 */
@Hidden
@Target(AnnotationTarget.VALUE_PARAMETER) // 생성자 매개변수에 적용
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginUser
