package com.hexagonal.domain.common.constant

enum class OrderSequenceType(
    val sequence: String,
) {
    // 오름차순
    ASC(OrderConstant.ASC),

    // 내림차순
    DESC(OrderConstant.DESC),
}
