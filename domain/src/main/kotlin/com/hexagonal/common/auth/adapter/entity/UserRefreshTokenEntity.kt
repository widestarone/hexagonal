package com.hexagonal.common.auth.adapter.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * refresh 토큰 저장용
 */
@Entity
@Table(name = "company_user_refresh_tokens")
class UserRefreshTokenEntity(
    @Id
    @Column(name = "user_key")
    private val key: String,
    @Column(name = "refresh_token")
    var value: String,
) {
    /**
     * 토큰 update
     */
    fun updateValue(token: String): UserRefreshTokenEntity {
        value = token
        return this
    }
}
