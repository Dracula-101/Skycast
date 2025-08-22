package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastWeatherInfo(
    val location: ForecastLocation,
    val days: List<ForecastDayInfo>,
    val description: String,
) : Parcelable