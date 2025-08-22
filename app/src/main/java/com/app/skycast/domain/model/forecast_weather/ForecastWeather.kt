package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.current_weather.CurrentWeather
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastWeather(
    val forecast: List<ForecastDay>?,
    val location: UserLocation?,
    val currentInfo: CurrentWeather?
): Parcelable