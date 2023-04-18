package com.hexagonal.domain.user.util

import com.hexagonal.domain.user.util.SubscribeDateUtil.plusMonth
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@DisplayName("구독 Date 유틸 테스트")
internal class SubscribeDateUtilTest {

    @Test
    fun `plusMonth 평년 2월 28일로 시작해서 말일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 2, 28, 18, 0, 0)

        plusMonth(baseDate, 1) shouldBe LocalDateTime.of(2022, 3, 28, 18, 0, 0)
        plusMonth(baseDate, 2) shouldBe LocalDateTime.of(2022, 4, 28, 18, 0, 0)
        plusMonth(baseDate, 3) shouldBe LocalDateTime.of(2022, 5, 28, 18, 0, 0)
    }

    @Test
    fun `plusMonth 윤년 2월 29일로 시작해서 말일 더하기`() {
        val baseDate = LocalDateTime.of(2024, 2, 29, 18, 0, 0)

        plusMonth(baseDate, 1) shouldBe LocalDateTime.of(2024, 3, 29, 18, 0, 0)
        plusMonth(baseDate, 2) shouldBe LocalDateTime.of(2024, 4, 29, 18, 0, 0)
        plusMonth(baseDate, 3) shouldBe LocalDateTime.of(2024, 5, 29, 18, 0, 0)
    }

    @Test
    fun `plusMonth 2022년 2월 28일에 24개월 더하면 2024년 2월 28일 반환`() {
        val baseDate = LocalDateTime.of(2022, 2, 28, 18, 0, 0)

        val result = plusMonth(baseDate, 24)

        result shouldBe LocalDateTime.of(2024, 2, 28, 18, 0, 0)
    }

    @Test
    fun `plusMonth 2022년 2월 28일에 12개월 더하면 2023년 2월 28일 반환`() {
        val baseDate = LocalDateTime.of(2022, 2, 28, 18, 0, 0)

        val result = plusMonth(baseDate, 12)

        result shouldBe LocalDateTime.of(2023, 2, 28, 18, 0, 0)
    }

    @Test
    fun `plusMonth 2024년 2월 29일에 48개월 더하면 2028년 2월 29일 반환`() {
        val baseDate = LocalDateTime.of(2024, 2, 29, 18, 0, 0)

        val result = plusMonth(baseDate, 48)

        result shouldBe LocalDateTime.of(2028, 2, 29, 18, 0, 0)
    }

    @Test
    fun `plusMonth 2024년 2월 29일에 12개월 더하면 2025년 2월 28일 반환`() {
        val baseDate = LocalDateTime.of(2024, 2, 29, 18, 0, 0)

        val result = plusMonth(baseDate, 12)

        result shouldBe LocalDateTime.of(2025, 2, 28, 18, 0, 0)
    }

    @Test
    fun `1월 31일로 시작해서 2월 제외 말일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 1, 31, 18, 0, 0)

        plusMonth(baseDate, 2) shouldBe LocalDateTime.of(2022, 3, 31, 18, 0, 0)
        plusMonth(baseDate, 3) shouldBe LocalDateTime.of(2022, 4, 30, 18, 0, 0)
        plusMonth(baseDate, 4) shouldBe LocalDateTime.of(2022, 5, 31, 18, 0, 0)
        plusMonth(baseDate, 5) shouldBe LocalDateTime.of(2022, 6, 30, 18, 0, 0)
        plusMonth(baseDate, 6) shouldBe LocalDateTime.of(2022, 7, 31, 18, 0, 0)
    }

    @Test
    fun `1월 31일로 시작해서 2월 말일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 1, 31, 18, 0, 0)

        plusMonth(baseDate, 1) shouldBe LocalDateTime.of(2022, 2, 28, 18, 0, 0)
        plusMonth(baseDate, 13) shouldBe LocalDateTime.of(2023, 2, 28, 18, 0, 0)
        plusMonth(baseDate, 25) shouldBe LocalDateTime.of(2024, 2, 29, 18, 0, 0)
    }

    @Test
    fun `1월 30일로 시작해서 2월 제외 말일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 1, 30, 18, 0, 0)

        plusMonth(baseDate, 2) shouldBe LocalDateTime.of(2022, 3, 30, 18, 0, 0)
        plusMonth(baseDate, 3) shouldBe LocalDateTime.of(2022, 4, 30, 18, 0, 0)
    }

    @Test
    fun `1월 30일로 시작해서 2월 말일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 1, 30, 18, 0, 0)

        plusMonth(baseDate, 1) shouldBe LocalDateTime.of(2022, 2, 28, 18, 0, 0)
        plusMonth(baseDate, 13) shouldBe LocalDateTime.of(2023, 2, 28, 18, 0, 0)
        plusMonth(baseDate, 25) shouldBe LocalDateTime.of(2024, 2, 29, 18, 0, 0)
    }

    @Test
    fun `1월 29일로 시작해서 2월 제외 말일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 1, 29, 18, 0, 0)

        plusMonth(baseDate, 2) shouldBe LocalDateTime.of(2022, 3, 29, 18, 0, 0)
        plusMonth(baseDate, 3) shouldBe LocalDateTime.of(2022, 4, 29, 18, 0, 0)
    }

    @Test
    fun `1월 29일로 시작해서 2월 말일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 1, 29, 18, 0, 0)

        plusMonth(baseDate, 1) shouldBe LocalDateTime.of(2022, 2, 28, 18, 0, 0)
        plusMonth(baseDate, 13) shouldBe LocalDateTime.of(2023, 2, 28, 18, 0, 0)
        plusMonth(baseDate, 25) shouldBe LocalDateTime.of(2024, 2, 29, 18, 0, 0)
    }

    @Test
    fun `월의 중간일 더하기`() {
        val baseDate = LocalDateTime.of(2022, 2, 14, 18, 0, 0)

        plusMonth(baseDate, 1) shouldBe LocalDateTime.of(2022, 3, 14, 18, 0, 0)
        plusMonth(baseDate, 2) shouldBe LocalDateTime.of(2022, 4, 14, 18, 0, 0)
        plusMonth(baseDate, 3) shouldBe LocalDateTime.of(2022, 5, 14, 18, 0, 0)
        plusMonth(baseDate, 4) shouldBe LocalDateTime.of(2022, 6, 14, 18, 0, 0)
    }
}
