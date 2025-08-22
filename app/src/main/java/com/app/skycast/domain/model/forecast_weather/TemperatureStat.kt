package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.units.Temperature
import kotlinx.parcelize.Parcelize

@Parcelize
class TemperatureStat(
    val min: Temperature,
    val max: Temperature,
    val avg: Temperature
): Parcelable