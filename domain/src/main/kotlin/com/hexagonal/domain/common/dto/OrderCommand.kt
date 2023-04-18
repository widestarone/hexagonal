package com.hexagonal.domain.common.dto

import com.hexagonal.domain.common.constant.OrderCriteriaType
import com.hexagonal.domain.common.constant.OrderSequenceType

data class OrderCommand(
    val criteriaType: OrderCriteriaType,
    val sequenceType: OrderSequenceType,
)
