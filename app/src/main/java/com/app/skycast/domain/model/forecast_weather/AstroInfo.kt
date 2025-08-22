package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AstroInfo(
    val sunrise: Date,
    val sunset: Date,
    val moonphase: String,
) : Parcelable