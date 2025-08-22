package com.app.skycast.domain.model.forecast_weather

import android.os.Parcelable
import com.app.skycast.domain.model.units.AirPressure
import com.app.skycast.domain.model.units.Precipitation
import com.app.skycast.domain.model.units.Wind
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ForecastDayInfo(
    val localTime: Date,
    val tempStat: TemperatureStat,
    val feelsLikeTempStat: TemperatureStat,
    val dew: Double,
    val humidity: Int,
    val precipitation: Precipitation,
    val windGust: Wind,
    val windSpeed: Wind,
    val windDirection: Double,
    val pressure: AirPressure,
    val visibility: Double,
    val uvIndex: Int,
    val astroInfo: AstroInfo,
    val hourInfo: List<ForecastHourInfo>,
) : Parcelable