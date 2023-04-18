package com.klleon.klonesales.common.util.encrypt

import java.security.MessageDigest

fun encryptSha256(input: String): String {
    return MessageDigest
        .getInstance("SHA-256")
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}
