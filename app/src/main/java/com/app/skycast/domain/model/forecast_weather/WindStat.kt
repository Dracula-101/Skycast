package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.units.Wind
import kotlinx.parcelize.Parcelize

@Parcelize
class WindStat(
    val min: Wind,
    val max: Wind,
): Parcelable