package com.hexagonal.common.util.date

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {
    val YYYY_MM_DD_HH_MM_SS_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

    fun LocalDateTime.isToday(): Boolean = this.toLocalDate().isToday()

    fun LocalDateTime.isTomorrow(): Boolean = this.toLocalDate().isTomorrow()

    fun LocalDateTime.isYesterday(): Boolean = this.toLocalDate().isYesterday()

    fun LocalDateTime.isPast(): Boolean = this.isBefore(LocalDateTime.now())

    fun LocalDateTime.isPastWithToday(): Boolean = this.isBefore(LocalDateTime.now()) || this.isToday()

    /**
     * 현재 시간으로부터 X분 이내 시간인지 비교
     * ex. 현재 시간이 12시이고 minute 값이 30이면 비교하는 시간이 11시40분일경우 true, 11시20분일경우 false
     */
    fun LocalDateTime.withInMinutes(minute: Long): Boolean {
        return !this.isBefore(LocalDateTime.now().minusMinutes(minute)) && !this.isAfter(LocalDateTime.now())
    }

    fun LocalDate.isToday(): Boolean = this.isEqual(LocalDate.now())

    fun LocalDate.isTomorrow(): Boolean = this.isEqual(LocalDate.now().plusDays(1))

    fun LocalDate.isYesterday(): Boolean = this.isEqual(LocalDate.now().minusDays(1))

    fun LocalDate.isPast(): Boolean = this.isBefore(LocalDate.now())

    fun LocalDate.isPastWithToday(): Boolean = this.isBefore(LocalDate.now()) || this.isToday()

    fun LocalDate.isFuture(): Boolean = this.isAfter(LocalDate.now())
}
