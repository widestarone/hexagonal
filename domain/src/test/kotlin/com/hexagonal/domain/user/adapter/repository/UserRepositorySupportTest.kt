package com.hexagonal.domain.user.adapter.repository

import com.hexagonal.domain.BaseTest
import com.hexagonal.domain.common.constant.IndustryType
import com.hexagonal.domain.user.adapter.entity.SubscribePlanEntity
import com.hexagonal.domain.user.adapter.entity.UserEntity
import com.hexagonal.domain.user.application.port.out.UserListCommand
import com.hexagonal.domain.user.application.port.out.UserSubscribeListCommand
import com.hexagonal.domain.user.constant.SubscribePlanType
import com.hexagonal.domain.user.constant.UserStatus
import com.hexagonal.domain.util.Randomizer.Companion.makeRandomInstance
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class UserRepositorySupportTest(
    @Autowired val userRepository: UserRepository,
    @Autowired val userRepositorySupport: UserRepositorySupport,
) : BaseTest() {

    val email = "test@email.com"
    val userLastName = "김"
    val userFirstName = "이름"
    val companyName = "회사 이름"
    val userStatus = UserStatus.ACTIVE
    val now = LocalDateTime.now()

    @BeforeEach
    fun beforeEach() {
        val userEntity = UserEntity(
            id = 1,
            userLastName = userLastName,
            userFirstName = userFirstName,
            email = email,
            password = makeRandomInstance(),
            lastLoginAt = now,
        )
        userRepository.save(userEntity)
    }

    @Test
    fun `email로 조회`() {
        val command = UserListCommand(email = email)
        val result = userRepositorySupport.findUserList(command)
        result.content.firstOrNull() shouldNotBe null
    }

    @Test
    fun `userLastName으로 조회`() {
        val command = UserListCommand(userLastName = userLastName)
        val result = userRepositorySupport.findUserList(command)
        result.content.firstOrNull() shouldNotBe null
    }

    @Test
    fun `userFirstName으로 조회`() {
        val partialUserFirstName = "이"
        val command = UserListCommand(userFirstName = partialUserFirstName)
        val result = userRepositorySupport.findUserList(command)
        result.content.firstOrNull() shouldNotBe null
    }

    @Test
    fun `userStatus로 조회`() {
        val command = UserListCommand(userStatus = userStatus)
        val result = userRepositorySupport.findUserList(command)
        result.content.firstOrNull() shouldNotBe null
    }

    @Test
    fun `lastLoginAt 구간으로 조회`() {
        val command = UserListCommand(startLastLoginAt = now.minusDays(1), endLastLoginAt = now.plusDays(1))
        val result = userRepositorySupport.findUserList(command)
        result.content.firstOrNull() shouldNotBe null
    }

    @Test
    fun `createdAt 구간으로 조회`() {
        val command = UserListCommand(startCreatedAt = now.minusDays(1), endLastLoginAt = now.plusDays(1))
        val result = userRepositorySupport.findUserList(command)
        result.content.firstOrNull() shouldNotBe null
    }
}
