package com.hexagonal.common.auth.helper.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.RSAKeyProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * JWT 관련 (토큰생성, 검증)
 */
@Component
class CognitoJwtUtils() {
    private val log: Logger get() = LoggerFactory.getLogger(this::class.java)
    private val jwt = JWT()

    /**
     * 토큰 검증
     */
    fun verifyToken(token: String, rsaKeyProvider: RSAKeyProvider): Boolean {
        return getDecodeJwtAndVerify(token, rsaKeyProvider) != null
    }

    fun readTokenPayload(token: String, rsaKeyProvider: RSAKeyProvider): Map<String, Any> {
        return getDecodeJwtAndVerify(token, rsaKeyProvider)!!.claims
    }

    private fun getDecodeJwtAndVerify(token: String, rsaKeyProvider: RSAKeyProvider): DecodedJWT? {
        val algorithm = Algorithm.RSA256(rsaKeyProvider)
        val jwtVerifier = JWT.require(algorithm).build()

        try {
            return jwtVerifier.verify(token)
        } catch (e: TokenExpiredException) {
            log.warn("토큰 만료. msg - {}", e.message)
            throw e
        } catch (e: JWTVerificationException) {
            log.error("잘못된 JWT 서명입니다. msg - {}", e.message)
        }

        return null
    }

    fun getUserIdWithoutVerify(token: String): Long? {
        return getDecodeJwt(token)?.claims?.get("username")?.asString()?.toLong()
    }
    private fun getDecodeJwt(token: String): DecodedJWT? {
        try {
            return jwt.decodeJwt(token)
        } catch (e: JWTDecodeException) {
            log.error("cannot decode this json web token msg - {}", e.message)
        }

        return null
    }
}
