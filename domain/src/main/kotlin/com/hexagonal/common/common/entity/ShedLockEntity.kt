package com.hexagonal.common.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * 스케줄러 락 처리 라이브러리(shedLock) 에서
 * 스케줄러 락 관리를 위해 사용하는 테이블
 * 코드로 직접 접근하는 로직은 없어야함.
 * https://www.baeldung.com/shedlock-spring
 */
@Entity
@Table(name = "shedlock")
class ShedLockEntity(

    @Id
    @Column(name = "name", nullable = false, length = 64)
    var name: String? = null,

    @Column(name = "lock_until", nullable = true, length = 3, columnDefinition = "TIMESTAMP(3)")
    var lockUntil: LocalDateTime? = null,

    @Column(name = "locked_at", nullable = true, length = 3, columnDefinition = "TIMESTAMP(3)")
    var lockedAt: LocalDateTime? = null,

    var locked_by: String,

)
