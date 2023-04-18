package com.hexagonal.common.util

import java.sql.Time
import java.time.Duration
import java.time.LocalDateTime

class TimeUtils {

    companion object {

        fun getTimeDuration(start: LocalDateTime?, end: LocalDateTime?): Time {
            if (start == null || end == null) {
                return Time.valueOf("0")
            }

            val diff = Duration.between(start, end)
            val timeString = String.format(
                "%d:%02d:%02d",
                diff.toHours(),
                diff.toMinutesPart(),
                diff.toSecondsPart(),
            )

            return Time.valueOf(timeString)
        }
    }
}
