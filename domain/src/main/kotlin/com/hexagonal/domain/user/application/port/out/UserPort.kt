package com.hexagonal.domain.user.application.port.out

import com.hexagonal.domain.company.adapter.entity.CompanyEntity
import com.hexagonal.domain.user.adapter.entity.SubscribePlanEntity
import com.hexagonal.domain.user.adapter.entity.UserEntity
import com.hexagonal.domain.user.domain.dto.CompanyUser
import org.springframework.data.domain.Page

interface UserPort {
    fun save(request: UserCreateCommand): UserEntity?
    fun findEntityById(id: Long): UserEntity?
    fun update(request: UserUpdateCommand): UserEntity?
    fun updatePassword(userPasswordUpdateCommand: UserPasswordUpdateCommand): Boolean
    fun findValidEntityByEmail(email: String): UserEntity?
    fun findEntityByEmail(email: String): UserEntity?
    fun findUserEntityList(command: UserListCommand): Page<UserEntity>
    fun updateUserStatus(command: UserStatusUpdateCommand): Boolean
}
