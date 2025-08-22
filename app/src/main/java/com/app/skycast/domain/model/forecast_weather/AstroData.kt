package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AstroData(
    val sunrise: Date,
    val sunset: Date,
    val moonrise: Date,
    val moonset: Date,
    val moonPhase: String,
    val moonIllumination: Int,
    val isMoonUp: Boolean,
    val isSunUp: Boolean,
) : Parcelable