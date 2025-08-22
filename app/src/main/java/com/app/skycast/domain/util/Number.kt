package com.app.skycast.domain.util

fun Float.precision(precision: Int): Float {
    return "%.${precision}f".format(this).toFloat()
}

fun Double.precision(precision: Int): Double {
    return "%.${precision}f".format(this).toDouble()
}