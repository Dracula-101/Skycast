package com.app.skycast.domain.model.current_weather

import android.os.Parcelable
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.units.AirPressure
import com.app.skycast.domain.model.units.Precipitation
import com.app.skycast.domain.model.units.Temperature
import com.app.skycast.domain.model.units.WindInfo
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class CurrentWeather(
    val location: UserLocation?,
    val localTime: Date,
    val temp: Temperature,
    val feelLikeTemp: Temperature?,
    val isDay: Boolean,
    val condition: WeatherConditionType,
    val icon: String,
    val wind: WindInfo?,
    val pressure: AirPressure?,
    val precipitation: Precipitation?,
    val humidity: Int?,
    val cloud: Int?,
    val uv: Double?,
) : Parcelable
