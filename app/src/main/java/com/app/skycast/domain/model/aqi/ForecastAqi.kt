package com.app.skycast.domain.model.aqi

import android.os.Parcelable
import com.app.skycast.domain.model.units.AirQuality
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastAqi(
    val currentDay: AirQuality,
    val forecast: List<DayAqi>,
) : Parcelable