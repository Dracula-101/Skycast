package com.app.skycast.domain.model.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wind(
    val kph: Float,
    val mph: Float,
): Parcelable, Unit() {

    override val metric: Float
        get() = kph
    override val imperial: Float
        get() = mph

    override fun display(unit: UnitType, addSpace: Boolean): String {
        val space = if (addSpace) " " else ""
        return when (unit) {
            UnitType.METRIC -> "${kph.toInt()}$space km/h"
            UnitType.IMPERIAL -> "${mph.toInt()}$space mph"
        }
    }

    companion object {
        val EMPTY: Wind = Wind(0.0f, 0.0f)

        fun fromUnit(unit: UnitType, value: Float): Wind {
            val conversionRatio = 0.621371f
            return when (unit) {
                UnitType.METRIC -> Wind(value, value * conversionRatio)
                UnitType.IMPERIAL -> Wind(value / conversionRatio, value)
            }
        }
    }

}