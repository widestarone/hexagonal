package com.hexagonal.common.auth.domain.helper

import java.security.SecureRandom

object AuthCodeGenerator {
    private const val BOUNDARY = 1000000
    private const val AUTH_CODE_FORMAT = "%06d"

    fun generateCode(): String {
        return String.format(
            AUTH_CODE_FORMAT,
            SecureRandom.getInstanceStrong().nextInt(BOUNDARY),
        )
    }
}
