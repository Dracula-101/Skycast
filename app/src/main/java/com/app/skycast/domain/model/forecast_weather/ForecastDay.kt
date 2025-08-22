package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.current_weather.WeatherConditionType
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ForecastDay(
    val updates: List<ForecastInfo>,
    val date: Date,
    val weatherCondition: WeatherConditionType,
    val astro: AstroData,
    val stats: ForecastStats,
) : Parcelable