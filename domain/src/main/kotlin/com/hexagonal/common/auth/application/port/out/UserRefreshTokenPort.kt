package com.hexagonal.common.auth.application.port.out

import com.hexagonal.common.auth.adapter.entity.UserRefreshTokenEntity

interface UserRefreshTokenPort {
    fun save(request: UserRefreshTokenCreateCommand)
    fun findByKey(id: String): UserRefreshTokenEntity?
    fun updateToken(entity: UserRefreshTokenEntity, token: String)
}
