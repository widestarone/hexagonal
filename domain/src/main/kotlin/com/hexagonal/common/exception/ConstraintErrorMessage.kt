package com.hexagonal.common.exception

data class ConstraintErrorMessage(
    val property: String,
    val message: String,
)
