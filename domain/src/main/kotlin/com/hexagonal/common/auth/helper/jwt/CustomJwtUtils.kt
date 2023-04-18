package com.hexagonal.common.auth.helper.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.security.Key
import java.util.*

/**
 * JWT 관련 (토큰생성, 검증)
 */
@Component
class CustomJwtUtils(
    @Value("\${custom-jwt.secret}")
    val secretKey: String,
) {
    private val log: Logger get() = LoggerFactory.getLogger(this::class.java)

    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    private val BEARER_TYPE = "Bearer"

    /**
     * 단순 토큰 생성
     */
    fun generateSimpleToken(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun generateToken(subject: String, id: Long, email: String, expiresIn: Long): String {
        return Jwts.builder()
            .setSubject(subject)
            .claim("email", email)
            .claim("id", id)
            .setExpiration(Date(System.currentTimeMillis() + expiresIn))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    /**
     * 토큰 검증
     */
    fun verifyToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            log.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            log.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            log.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            log.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            log.info("JWT 토큰이 잘못되었습니다.")
        }
        return false
    }

    /**
     * 토큰에서 claim 추출
     */
    fun getClaim(token: String, key: String): String? = getAllClaims(token)[key].toString()

    /**
     * 토큰에서 claim 전체 추출
     */
    private fun getAllClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}
