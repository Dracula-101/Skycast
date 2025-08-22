package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.current_weather.WeatherConditionType
import com.app.skycast.domain.model.units.Precipitation
import com.app.skycast.domain.model.units.Temperature
import com.app.skycast.domain.model.units.Wind
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ForecastHourInfo(
    val time: Date,
    val temperature: Temperature,
    val feelsLike: Temperature,
    val humidity: Double,
    val precipitation: Precipitation,
    val windGust: Wind,
    val windSpeed: Wind,
    val windDirection: Double,
    val uvIndex: Int,
    val weatherCondition: WeatherConditionType,
    val visibility: Double,
    val weatherDescription: String,
    val isDay: Boolean,
) : Parcelable
