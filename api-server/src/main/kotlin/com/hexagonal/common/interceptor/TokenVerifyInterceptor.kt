package com.hexagonal.common.interceptor

import com.auth0.jwt.exceptions.TokenExpiredException
import com.hexagonal.common.exception.AuthenticationException
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.common.logging.Log
import com.hexagonal.domain.user.domain.service.UserService
import com.hexagonal.common.jwt.TokenHttpRequestUtil
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *  토큰 검증용 인터셉터
 */
@Component
class TokenVerifyInterceptor(
    private val userService: UserService,
    private val tokenHttpRequestUtil: TokenHttpRequestUtil,
) : HandlerInterceptor, Log {
    companion object {
        const val profileKey = "userProfile"
    }

    /**
     * 토큰 검증
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
//        val method = request.method
//        if (method.equals(HttpMethod.OPTIONS.name)) {
//            return true
//        }
//
//        try {
//            val token: String = tokenHttpRequestUtil.getToken(request)
//            val userId = tokenHttpRequestUtil.getUserId(token)
//            val user = userService.findUser(userId)
//                ?: throw AuthenticationException(ErrorCode.INVALID_TOKEN_ERROR)
//
//            request.setAttribute(profileKey, user)
//            log.info("request userId: {}. token: {}", userId, token)
//        } catch (e: TokenExpiredException) {
//            log.warn("access token is expired. msg {}.", e.message)
//            throw AuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED)
//        } catch (e: Exception) {
//            log.error(
//                "error occurs on verifying access token. request uri - {}, authorization header value - {} msg - {}",
//                request.requestURI,
//                request.getHeader("Authorization"),
//                e.message,
//            )
//            throw AuthenticationException(ErrorCode.INVALID_TOKEN_ERROR, e)
//        }
        return true
    }
}
