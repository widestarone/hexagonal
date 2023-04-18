package com.hexagonal.domain.common.constant

enum class OrderCriteriaType(
    val column: String,
) {
    BY_ORDER_INDEX(OrderConstant.ORDER_INDEX_COLUMN),
    BY_ID(OrderConstant.ID_COLUMN),
}
