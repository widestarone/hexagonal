package com.hexagonal.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Log {
    val log: Logger get() = LoggerFactory.getLogger(this::class.java.simpleName)
}
