package com.app.skycast.domain.model.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Temperature(
    override val celcius: Float,
    override val fahrenheit: Float
) : Parcelable, Degree() {

    override val metric: Float
        get() = celcius

    override val imperial: Float
        get() = fahrenheit

    override fun display(
        unit: UnitType,
        addSpace: Boolean
    ): String {
        val space = if (addSpace) " " else ""
        return when (unit) {
            UnitType.METRIC -> "${celcius.toInt()}$space°C"
            UnitType.IMPERIAL -> "${fahrenheit.toInt()}$space°F"
        }
    }

    companion object {
        val EMPTY: Temperature = Temperature(0.0f, 0.0f)

        fun fromUnit(unit: UnitType, value: Float): Temperature {
            return when (unit) {
                UnitType.METRIC -> Temperature(value, value * 9 / 5 + 32)
                UnitType.IMPERIAL -> Temperature((value - 32) * 5 / 9, value)
            }
        }
    }
}

