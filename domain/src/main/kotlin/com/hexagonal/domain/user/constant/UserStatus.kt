package com.hexagonal.domain.user.constant

enum class UserStatus {
    /**
     * 활성
     */
    ACTIVE,

    /**
     * 휴면
     */
    STOPPED,

    /**
     * 탈퇴
     */
    DELETED,

    /**
     * 영구 정지
     */
    PERMANENT_BANNED,
}
