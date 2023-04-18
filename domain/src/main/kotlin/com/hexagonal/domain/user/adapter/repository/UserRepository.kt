package com.hexagonal.domain.user.adapter.repository

import com.hexagonal.domain.user.adapter.entity.UserEntity
import com.hexagonal.domain.user.constant.UserStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findEntityByEmail(email: String): UserEntity?

    @Modifying
    @Query("UPDATE UserEntity user SET user.password = :newPassword WHERE user.id = :id")
    fun updatePassword(id: Long, newPassword: String)

    @Modifying
    @Query("UPDATE UserEntity user SET user.deviceEndpoint = :deviceEndpoint WHERE user.id = :id")
    fun updateDeviceEndpoint(id: Long, deviceEndpoint: String)

    @Modifying
    @Query("UPDATE UserEntity user SET user.userStatus = :userStatus WHERE user.id = :id")
    fun updateUserStatus(id: Long, userStatus: UserStatus): Int
}
