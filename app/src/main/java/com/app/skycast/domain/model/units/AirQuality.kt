package com.app.skycast.domain.model.units

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.Parcelize

@Parcelize
data class AirQuality(
    val value: Double,
    val co: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm2_5: Double,
    val pm10: Double,
) : Parcelable {

    private fun getCOMax(): Double = 30.4
    private fun getNO2Max(): Double = 1249.0
    private fun getO3Max(): Double = 404.0
    private fun getSO2Max(): Double = 604.0
    private fun getPM25Max(): Double = 250.4
    private fun getPM10Max(): Double = 424.0

    // Get the percentage of max value for each pollutant
    fun getCOPercentage(): Double = (co / getCOMax() * 100).coerceIn(0.0, 100.0)
    fun getNO2Percentage(): Double = (no2 / getNO2Max() * 100).coerceIn(0.0, 100.0)
    fun getO3Percentage(): Double = (o3 / getO3Max() * 100).coerceIn(0.0, 100.0)
    fun getSO2Percentage(): Double = (so2 / getSO2Max() * 100).coerceIn(0.0, 100.0)
    fun getPM25Percentage(): Double = (pm2_5 / getPM25Max() * 100).coerceIn(0.0, 100.0)
    fun getPM10Percentage(): Double = (pm10 / getPM10Max() * 100).coerceIn(0.0, 100.0)

    fun toDescription(): String {
        val sections = listOf(50, 100, 150, 200, 300)
        val descriptions = listOf(
            "Good air quality",
            "Moderate air quality",
            "Unhealthy for Sensitive Groups air quality",
            "Unhealthy air quality",
            "Very Unhealthy air quality",
            "Hazardous air quality",
        )
        // check aqi is closer to section
        val closestSection = sections.minByOrNull { Math.abs(value - it) } ?: 0
        val index = sections.indexOf(closestSection)
        return descriptions[index]
    }
    fun toLongDescription(): String {
        return when {
            value <= 50 -> "Air quality is considered satisfactory, and air pollution poses little or no risk."
            value <= 100 -> "Air quality is acceptable; however, for some pollutants there may be a moderate health concern for a very small number of people who are unusually sensitive to air pollution."
            value <= 150 -> "Members of sensitive groups may experience health effects. The general public is not likely to be affected."
            value <= 200 -> "Everyone may begin to experience health effects; members of sensitive groups may experience more serious health effects."
            value <= 300 -> "Health alert: everyone may experience more serious health effects."
            else -> "Health warnings of emergency conditions. The entire population is more likely to be affected."
        }
    }

    fun toColor(): Color {
        val colorInt = when {
            value <= 50 -> 0xFF01E500.toInt() // Green
            value <= 100 -> 0xFFFEFF01.toInt() // Yellow
            value <= 150 -> 0xFFFF7F01.toInt() // Orange
            value <= 200 -> 0xFFFE0001.toInt() // Red
            value <= 300 -> 0xFF8E3E97.toInt() // Purple
            else -> 0xFF7F0123.toInt() // Maroon
        }
        return Color(colorInt)
    }

    fun getCOColor(): Color {
        return when {
            co <= 4.4 -> Color(0xFF01E500) // Good
            co <= 9.4 -> Color(0xFFFEFF01) // Moderate
            co <= 12.4 -> Color(0xFFFF7F01) // Unhealthy for Sensitive Groups
            co <= 15.4 -> Color(0xFFFE0001) // Unhealthy
            co <= 30.4 -> Color(0xFF8E3E97) // Very Unhealthy
            else -> Color(0xFF7F0123) // Hazardous
        }
    }

    fun getNO2Color(): Color {
        return when {
            no2 <= 53 -> Color(0xFF01E500) // Good
            no2 <= 100 -> Color(0xFFFEFF01) // Moderate
            no2 <= 360 -> Color(0xFFFF7F01) // Unhealthy for Sensitive Groups
            no2 <= 649 -> Color(0xFFFE0001) // Unhealthy
            no2 <= 1249 -> Color(0xFF8E3E97) // Very Unhealthy
            else -> Color(0xFF7F0123) // Hazardous
        }
    }

    fun getO3Color(): Color {
        return when {
            o3 <= 54 -> Color(0xFF01E500) // Good
            o3 <= 124 -> Color(0xFFFEFF01) // Moderate
            o3 <= 164 -> Color(0xFFFF7F01) // Unhealthy for Sensitive Groups
            o3 <= 204 -> Color(0xFFFE0001) // Unhealthy
            o3 <= 404 -> Color(0xFF8E3E97) // Very Unhealthy
            else -> Color(0xFF7F0123) // Hazardous
        }
    }

    fun getSO2Color(): Color {
        return when {
            so2 <= 35 -> Color(0xFF01E500) // Good
            so2 <= 75 -> Color(0xFFFEFF01) // Moderate
            so2 <= 185 -> Color(0xFFFF7F01) // Unhealthy for Sensitive Groups
            so2 <= 304 -> Color(0xFFFE0001) // Unhealthy
            so2 <= 604 -> Color(0xFF8E3E97) // Very Unhealthy
            else -> Color(0xFF7F0123) // Hazardous
        }
    }

    fun getPM25Color(): Color {
        return when {
            pm2_5 <= 12.0 -> Color(0xFF01E500) // Good
            pm2_5 <= 35.4 -> Color(0xFFFEFF01) // Moderate
            pm2_5 <= 55.4 -> Color(0xFFFF7F01) // Unhealthy for Sensitive Groups
            pm2_5 <= 150.4 -> Color(0xFFFE0001) // Unhealthy
            pm2_5 <= 250.4 -> Color(0xFF8E3E97) // Very Unhealthy
            else -> Color(0xFF7F0123) // Hazardous
        }
    }

    fun getPM10Color(): Color {
        return when {
            pm10 <= 54 -> Color(0xFF01E500) // Good
            pm10 <= 154 -> Color(0xFFFEFF01) // Moderate
            pm10 <= 254 -> Color(0xFFFF7F01) // Unhealthy for Sensitive Groups
            pm10 <= 354 -> Color(0xFFFE0001) // Unhealthy
            pm10 <= 424 -> Color(0xFF8E3E97) // Very Unhealthy
            else -> Color(0xFF7F0123) // Hazardous
        }
    }

    companion object {

        val EMPTY: AirQuality = AirQuality(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        val colorGradientList = listOf(
            Color(0xFF01E500), // Green
            Color(0xFFFEFF01), // Yellow
            Color(0xFFFF7F01), // Orange
            Color(0xFFFE0001), // Red
            Color(0xFF8E3E97), // Purple
            Color(0xFF7F0123), // Maroon
        )
    }
}
