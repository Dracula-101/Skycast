package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastLocation(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val address: String,
) : Parcelable
