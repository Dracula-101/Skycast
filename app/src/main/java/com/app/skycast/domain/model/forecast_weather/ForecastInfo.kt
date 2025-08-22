package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.current_weather.WeatherConditionType
import com.app.skycast.domain.model.units.AirPressure
import com.app.skycast.domain.model.units.Precipitation
import com.app.skycast.domain.model.units.Temperature
import com.app.skycast.domain.model.units.WindInfo
import com.app.skycast.domain.model.units.Wind
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ForecastInfo(
    val time: Date,
    val temp: Temperature,
    val isDay: Boolean,
    val wind: WindInfo,
    val icon: String,
    val pressure: AirPressure,
    val precipitation: Precipitation,
    val snow: Float,
    val humidity: Int,
    val cloud: Int,
    val condition: WeatherConditionType,
    val feelsLikeTemp: Temperature,
    val windChill: Temperature,
    val dewPoint: Temperature,
    val willRain: Boolean,
    val chanceOfRain: Int,
    val willSnow: Boolean,
    val chanceOfSnow: Int,
    val uvIndex: Int,
    val gust: Wind,
) : Parcelable