package com.hexagonal.common.exception

open class ServiceException(val errorCode: ErrorCode, cause: Throwable?) : RuntimeException(errorCode.message, cause) {
    constructor(errorCode: ErrorCode) : this(errorCode, null)
}
