package com.app.skycast.domain.model.units

enum class UnitType {
    METRIC,
    IMPERIAL;

    override fun toString(): String {
        return when (this) {
            METRIC -> "metric"
            IMPERIAL -> "imperial"
        }
    }
}