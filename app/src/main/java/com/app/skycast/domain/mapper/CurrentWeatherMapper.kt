package com.app.skycast.domain.mapper

import com.app.skycast.data.model.current_weather.CurrentWeatherResponseDTO
import com.app.skycast.domain.model.units.AirPressure
import com.app.skycast.domain.model.current_weather.CurrentWeather
import com.app.skycast.domain.model.units.Precipitation
import com.app.skycast.domain.model.units.Temperature
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.current_weather.WeatherConditionType
import com.app.skycast.domain.model.units.WindInfo
import com.app.skycast.domain.model.units.Wind
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun CurrentWeatherResponseDTO.toDomainModel(): CurrentWeather {
    return CurrentWeather(
        location = UserLocation(
            country = location?.country ?: "",
            latitude = location?.lat ?: 0.0,
            longitude = location?.lon ?: 0.0,
            state = location?.region ?: "",
            city = location?.name ?: "",
            countryCode = "",
        ),
        localTime = run {
            try {
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(location?.localtime.toString())
            } catch (e: Exception) {
                Calendar.getInstance().time
            }
        }!!,
        temp = Temperature(
            celcius = (currentInfo?.tempC ?: 0.0).toFloat(),
            fahrenheit = (currentInfo?.tempF ?: 0.0).toFloat()
        ),
        feelLikeTemp = Temperature(
            celcius = (currentInfo?.feelslikeC ?: 0.0).toFloat(),
            fahrenheit = (currentInfo?.feelslikeF ?: 0.0).toFloat()
        ),
        isDay = currentInfo?.isDay == 1,
        condition = WeatherConditionType.fromCode(currentInfo?.condition?.code ?: 0),
        icon = "https:${(currentInfo?.condition?.icon)?.replace("64x64", "128x128")}",
        wind = WindInfo(
            degree = currentInfo?.windDegree ?: 0,
            direction = currentInfo?.windDir ?: "",
            speed = Wind(
                kph = (currentInfo?.windKph ?: 0.0).toFloat(),
                mph = (currentInfo?.windMph ?: 0.0).toFloat()
            )
        ),
        pressure = AirPressure(
            hPa = (currentInfo?.pressureMb ?: 0.0).toFloat(),
            mmHg = (currentInfo?.pressureIn ?: 0.0).toFloat()
        ),
        precipitation = Precipitation(
            mm = (currentInfo?.precipMm ?: 0.0).toFloat(),
            inch = (currentInfo?.precipIn ?: 0.0).toFloat()
        ),
        humidity = currentInfo?.humidity,
        cloud = currentInfo?.cloud,
        uv = currentInfo?.uv,
    )
}
