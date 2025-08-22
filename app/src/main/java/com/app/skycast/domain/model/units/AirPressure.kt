package com.app.skycast.domain.model.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AirPressure(
    override val hPa: Float,
    override val mmHg: Float
) : Parcelable, Pressure() {

    override val metric: Float
        get() = hPa
    override val imperial: Float
        get() = mmHg

    override fun display(unit: UnitType, addSpace: Boolean): String {
        val space = if (addSpace) " " else ""
        return when (unit) {
            UnitType.METRIC -> "${hPa.toInt()}$space hPa"
            UnitType.IMPERIAL -> "${mmHg.toInt()}$space mmHg"
        }
    }

    companion object {
        val EMPTY: AirPressure = AirPressure(0.0f, 0.0f)

        fun fromUnit(unit: UnitType, value: Float): AirPressure {
            val conversionRatio = 0.7500638f
            return when (unit) {
                UnitType.METRIC -> AirPressure(value, value * conversionRatio)
                UnitType.IMPERIAL -> AirPressure(value / conversionRatio, value)
            }
        }

    }
}