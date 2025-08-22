package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.units.Precipitation
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrecipitationStat(
    val total: Precipitation,
    val totalSnow: Float,
): Parcelable