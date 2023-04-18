package com.hexagonal.common.resolver

import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.exception.ServiceException
import com.hexagonal.domain.user.domain.dto.StudioSimpleUser
import com.hexagonal.common.annotation.LoginUser
import com.hexagonal.common.interceptor.TokenVerifyInterceptor
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

/**
 * 컨트롤러에 LoginUser annotation 존재시 값을 바인딩한다.
 */
@Component
class LoginStudioUserArgumentResolver : HandlerMethodArgumentResolver {

    /**
     * 컨트롤러에 LoginUser annotation 존재여부 확인
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    /**
     * LoginUser annotation 존재시 토큰을 확인하여 사용자의 정보를 바인딩한다
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): StudioSimpleUser {
        return webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?.getAttribute(TokenVerifyInterceptor.profileKey) as StudioSimpleUser?
            ?: throw ServiceException(ErrorCode.INVALID_TOKEN_ERROR)
    }
}
