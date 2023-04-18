package com.hexagonal.common.dto

data class MessageCompleteCommand(
    val taskId: Long,
    val success: Boolean,
    val message: String? = null,
)
