package com.hexagonal.domain.user.application.port.out

import com.hexagonal.common.constant.PageInfoConstant
import com.hexagonal.domain.user.constant.UserSortType
import com.hexagonal.domain.user.constant.UserStatus
import java.time.LocalDateTime

data class UserListCommand(
    val userFirstName: String? = null,
    val userLastName: String? = null,
    val email: String? = null,
    val userStatus: UserStatus? = null,
    val startCreatedAt: LocalDateTime? = null,
    val endCreatedAt: LocalDateTime? = null,
    val startLastLoginAt: LocalDateTime? = null,
    val endLastLoginAt: LocalDateTime? = null,
    val sortType: UserSortType = UserSortType.NONE,
    val isDesc: Boolean = true,
    val pageIndex: Int = PageInfoConstant.DEFAULT_PAGE_INDEX,
    val pageSize: Int = PageInfoConstant.DEFAULT_PAGE_SIZE,
)
