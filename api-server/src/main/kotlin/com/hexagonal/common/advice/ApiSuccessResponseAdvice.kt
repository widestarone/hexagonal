package com.hexagonal.common.advice

import com.hexagonal.common.annotation.SuccessApiResponse
import com.hexagonal.common.response.SuccessResponse
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ApiSuccessResponseAdvice<Any> : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        for (annotation in returnType.executable.declaringClass.annotations) {
            if (annotation.annotationClass == SuccessApiResponse::class) {
                return true
            }
        }

        return false
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        return SuccessResponse<Any?>(
            success = true,
            data = body,
        ) as Any?
    }
}
