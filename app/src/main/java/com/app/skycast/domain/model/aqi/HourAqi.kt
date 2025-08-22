package com.app.skycast.domain.model.aqi

import android.os.Parcelable
import com.app.skycast.domain.model.units.AirQuality
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class HourAqi(
    val airQuality: AirQuality,
    val date: Date,
) : Parcelable