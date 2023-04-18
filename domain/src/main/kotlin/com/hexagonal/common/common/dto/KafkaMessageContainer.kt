package com.hexagonal.common.dto

/**
 * 미디어 매니지먼트, 미디어 프로세싱 서버가 전달하는 메시지
 * 참고 : https://klleon.atlassian.net/wiki/spaces/TC/pages/103579656/S-02-02.+Consumer+Guide
 */
data class KafkaMessageContainer<T>(

    val taskType: String,
    val taskId: Long,

    /**
     * 일부 메시지에서 추가적으로 전달하는 데이터 내용을 위한 필드
     */
    val data: T?,

    val success: Boolean,
    val message: String? = null,
) {
    fun toMessageCompleteCommand() = MessageCompleteCommand(
        taskId = this.taskId,
        success = this.success,
        message = this.message,
    )
}
