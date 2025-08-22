package com.app.skycast.domain.model.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Precipitation(
    val mm: Float,
    val inch: Float
): Parcelable, Unit() {

    override val metric: Float
        get() = mm
    override val imperial: Float
        get() = inch

    override fun display(unit: UnitType, addSpace: Boolean): String {
        val space = if (addSpace) " " else ""
        return when (unit) {
            UnitType.METRIC -> "${mm.toInt()}$space mm"
            UnitType.IMPERIAL -> "${inch.toInt()}$space inch"
        }
    }

    companion object {
        val EMPTY: Precipitation = Precipitation(0.0f, 0.0f)

        fun fromUnit(unit: UnitType, value: Float): Precipitation {
            val conversionRatio = 0.0393701f
            return when (unit) {
                UnitType.METRIC -> Precipitation(value, value * conversionRatio)
                UnitType.IMPERIAL -> Precipitation(value / conversionRatio, value)
            }
        }
    }

}