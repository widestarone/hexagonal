package com.hexagonal.common.dto

open class PageContainer<T>(

    val page: Int,

    val pageSize: Int,

    val totalCount: Long,

    val hasNext: Boolean,

    val list: List<T>,

)
