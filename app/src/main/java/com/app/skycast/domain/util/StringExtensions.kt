package com.app.skycast.domain.util

fun String?.toStringSafe(): String {
    return if (this?.isNotBlank() == false) {
        ""
    } else {
        this ?: ""
    }
}