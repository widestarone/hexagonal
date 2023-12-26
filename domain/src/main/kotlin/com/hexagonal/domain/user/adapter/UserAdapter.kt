package com.hexagonal.domain.user.adapter

import com.hexagonal.common.logging.Log
import com.hexagonal.domain.user.adapter.entity.UserEntity
import com.hexagonal.domain.user.adapter.repository.UserRepository
import com.hexagonal.domain.user.adapter.repository.UserRepositorySupport
import com.hexagonal.domain.user.application.port.out.UserCreateCommand
import com.hexagonal.domain.user.application.port.out.UserListCommand
import com.hexagonal.domain.user.application.port.out.UserPasswordUpdateCommand
import com.hexagonal.domain.user.application.port.out.UserPort
import com.hexagonal.domain.user.application.port.out.UserStatusUpdateCommand
import com.hexagonal.domain.user.application.port.out.UserUpdateCommand
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserAdapter(
    private val userRepository: UserRepository,
    private val userRepositorySupport: UserRepositorySupport,
) : UserPort, Log {
    @Transactional
    override fun save(command: UserCreateCommand): UserEntity? {
        val userEntity = UserEntity(
            password = command.password,
            userLastName = command.userLastName,
            userFirstName = command.userFirstName,
            phoneNumber = command.phoneNumber,
            mobileNumber = command.mobileNumber,
            userType = command.userType,
            email = command.email,
        )

        return userRepository.save(userEntity)
    }

    @Transactional(readOnly = true)
    override fun findEntityById(id: Long): UserEntity? {
        val userEntity = userRepository.findByIdOrNull(id)
        // soft 삭제 시 회원 조회 안됨
        return when (userEntity?.deletedAt) {
            null -> userEntity
            else -> null
        }
    }

    @Transactional
    override fun update(request: UserUpdateCommand): UserEntity? {
        val userEntity = userRepository.findEntityByEmail(request.email) ?: return null
        return userEntity.update(request)
    }

    @Transactional
    override fun updatePassword(userPasswordUpdateCommand: UserPasswordUpdateCommand): Boolean {
        userRepository.updatePassword(
            userPasswordUpdateCommand.userId,
            userPasswordUpdateCommand.newPassword,
        )

        return true
    }

    override fun findValidEntityByEmail(email: String): UserEntity? {
        val userEntity = findEntityByEmail(email)

        return when (userEntity?.deletedAt) {
            null -> userEntity
            else -> null
        }
    }

    override fun findEntityByEmail(email: String): UserEntity? {
        return userRepository.findEntityByEmail(email)
    }

    @Transactional(readOnly = true)
    override fun findUserEntityList(command: UserListCommand): Page<UserEntity> {
        return userRepositorySupport.findUserList(command)
    }

    @Transactional
    override fun updateUserStatus(command: UserStatusUpdateCommand): Boolean {
        return userRepository.updateUserStatus(id = command.id, userStatus = command.userStatus) == 1
    }
}
