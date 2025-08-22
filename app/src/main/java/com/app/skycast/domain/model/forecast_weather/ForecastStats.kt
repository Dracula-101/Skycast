package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastStats(
    val tempStats: TemperatureStat,
    val windStats: WindStat,
    val precipitationStats: PrecipitationStat,
    val probability: ForecastProbability,
    val uvIndex: Int,
    val avgHumidity: Int,
) : Parcelable