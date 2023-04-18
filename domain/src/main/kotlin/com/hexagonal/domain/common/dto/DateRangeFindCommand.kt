package com.hexagonal.domain.common.dto

import java.time.LocalDateTime

data class DateRangeFindCommand(
    val startCreatedAt: LocalDateTime? = null,

    val endCreatedAt: LocalDateTime? = null,

    val startUpdatedAt: LocalDateTime? = null,

    val endUpdatedAt: LocalDateTime? = null,
)
