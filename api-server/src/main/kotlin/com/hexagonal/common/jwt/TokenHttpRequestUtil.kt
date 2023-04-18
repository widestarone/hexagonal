package com.hexagonal.common.jwt

import com.auth0.jwt.interfaces.Claim
import com.hexagonal.common.auth.helper.jwt.CognitoJwtUtils
import com.hexagonal.common.exception.AuthenticationException
import com.hexagonal.common.exception.ErrorCode
import com.hexagonal.domain.user.domain.helper.AwsCognitoRSAKeyProvider
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest

@Component
class TokenHttpRequestUtil(
    val cognitoJwtUtils: CognitoJwtUtils,
    val awsCognitoRSAKeyProvider: AwsCognitoRSAKeyProvider,
) {
    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    /**
     * http header token 취득
     */
    private fun getHttpRequestToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return when {
            StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX) -> bearerToken.substring(
                BEARER_PREFIX.length,
            )
            else -> throw AuthenticationException(ErrorCode.BAD_CREDENTIALS_ERROR)
        }
    }

    /**
     * 토큰 검증 및 토큰 취득
     */
    fun getToken(request: HttpServletRequest): String {
        val token: String = getHttpRequestToken(request)

        if (!cognitoJwtUtils.verifyToken(token, awsCognitoRSAKeyProvider)) {
            throw AuthenticationException(ErrorCode.BAD_CREDENTIALS_ERROR)
        }
        return token
    }

    /**
     * 사용자 ID 취득
     */
    fun getUserId(token: String): Long {
        val payload = cognitoJwtUtils.readTokenPayload(token, awsCognitoRSAKeyProvider)

        return (payload["username"] as Claim).asString().toLong()
    }
}
