package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastProbability(
    val willRain: Boolean,
    val chanceOfRain: Int,
    val willSnow: Boolean,
    val chanceOfSnow: Int,
) : Parcelable