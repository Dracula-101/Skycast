package com.app.skycast.domain.mapper

import com.app.skycast.data.model.forecast_alt.ForecastWeatherAltResponseDTO
import com.app.skycast.data.model.forecast_weather.ForecastWeatherResponseDTO
import com.app.skycast.domain.model.units.AirPressure
import com.app.skycast.domain.model.forecast_weather.AstroData
import com.app.skycast.domain.model.current_weather.CurrentWeather
import com.app.skycast.domain.model.forecast_weather.ForecastDay
import com.app.skycast.domain.model.forecast_weather.ForecastInfo
import com.app.skycast.domain.model.forecast_weather.ForecastProbability
import com.app.skycast.domain.model.forecast_weather.ForecastStats
import com.app.skycast.domain.model.forecast_weather.ForecastWeather
import com.app.skycast.domain.model.units.Precipitation
import com.app.skycast.domain.model.forecast_weather.PrecipitationStat
import com.app.skycast.domain.model.units.Temperature
import com.app.skycast.domain.model.forecast_weather.TemperatureStat
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.current_weather.WeatherConditionType
import com.app.skycast.domain.model.forecast_weather.AstroInfo
import com.app.skycast.domain.model.forecast_weather.ForecastDayInfo
import com.app.skycast.domain.model.forecast_weather.ForecastHourInfo
import com.app.skycast.domain.model.forecast_weather.ForecastLocation
import com.app.skycast.domain.model.forecast_weather.ForecastWeatherInfo
import com.app.skycast.domain.model.units.WindInfo
import com.app.skycast.domain.model.forecast_weather.WindStat
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.domain.model.units.Wind
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun ForecastWeatherResponseDTO.toDomainModel(): ForecastWeather {
    return ForecastWeather(
        location = UserLocation(
            country = location?.country ?: "",
            latitude = location?.lat ?: 0.0,
            longitude = location?.lon ?: 0.0,
            state = location?.region ?: "",
            city = location?.name ?: "",
            countryCode = "",
        ),
        forecast = forecast?.forecastday?.map { day ->
            ForecastDay(
                date = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).parse(day.date.toString()) ?: run {
                    val calendar = Calendar.getInstance()
                    calendar.time
                },
                astro = AstroData(
                    sunrise = run {
                        try {
                            SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).parse(
                                "${day.date} ${day.astro?.sunrise}"
                            )
                        } catch (e: ParseException) {
                            val calendar = Calendar.getInstance()
                            calendar.time
                        }
                    }!!,
                    sunset =  run {
                        try {
                            SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).parse(
                                "${day.date} ${day.astro?.sunset}"
                            )
                        } catch (e: ParseException) {
                            val calendar = Calendar.getInstance()
                            calendar.time
                        }
                    }!!,
                    moonrise = run {
                        try {
                            SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).parse(
                                "${day.date} ${day.astro?.moonrise}"
                            )
                        } catch (e: ParseException) {
                            val calendar = Calendar.getInstance()
                            calendar.time
                        }
                    }!!,
                    moonset = run {
                        try {
                            SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).parse(
                                "${day.date} ${day.astro?.moonset}"
                            )
                        } catch (e: ParseException) {
                            val calendar = Calendar.getInstance()
                            calendar.time
                        }
                    }!!,
                    moonPhase = day.astro?.moonPhase ?: "",
                    moonIllumination = day.astro?.moonIllumination ?: 0,
                    isMoonUp = day.astro?.isMoonUp == 1,
                    isSunUp = day.astro?.isSunUp == 1
                ),
                weatherCondition = WeatherConditionType.fromCode(day.day?.condition?.code ?: 0),
                stats = ForecastStats(
                    tempStats = TemperatureStat(
                        min = Temperature(
                            celcius = (day.day?.mintempC ?: 0.0).toFloat(),
                            fahrenheit = (day.day?.mintempF ?: 0.0).toFloat()
                        ),
                        max = Temperature(
                            celcius = (day.day?.maxtempC ?: 0.0).toFloat(),
                            fahrenheit = (day.day?.maxtempF ?: 0.0).toFloat()
                        ),
                        avg = Temperature(
                            celcius = (day.day?.avgtempC ?: 0.0).toFloat(),
                            fahrenheit = (day.day?.avgtempF ?: 0.0).toFloat()
                        )
                    ),
                    uvIndex = (day.day?.uv ?: 0.0).toInt(),
                    windStats = WindStat(
                        min = Wind(
                            kph = (day.day?.maxwindKph ?: 0.0).toFloat(),
                            mph = (day.day?.maxwindMph ?: 0.0).toFloat()
                        ),
                        max = Wind(
                            kph = (day.day?.maxwindKph ?: 0.0).toFloat(),
                            mph = (day.day?.maxwindMph ?: 0.0).toFloat()
                        ),
                    ),
                    avgHumidity = day.day?.avghumidity ?: 0,
                    probability = ForecastProbability(
                        willRain = day.day?.dailyWillItRain == 1,
                        chanceOfRain = day.day?.dailyChanceOfRain ?: 0,
                        willSnow = day.day?.dailyWillItSnow == 1,
                        chanceOfSnow = day.day?.dailyChanceOfSnow ?: 0
                    ),
                    precipitationStats = PrecipitationStat(
                        total = Precipitation(
                            mm = (day.day?.totalprecipMm ?: 0.0).toFloat(),
                            inch = (day.day?.totalprecipIn ?: 0.0).toFloat()
                        ),
                        totalSnow = (day.day?.totalsnowCm ?: 0.0).toFloat()
                    )
                ),
                updates = day.hour?.map { hour ->
                    ForecastInfo(
                        time = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm",
                            Locale.getDefault()
                        ).parse(hour.time.toString()) ?: run {
                            val calendar = Calendar.getInstance()
                            calendar.time
                        },
                        temp = Temperature(
                            celcius = (hour.tempC ?: 0.0).toFloat(),
                            fahrenheit = (hour.tempF ?: 0.0).toFloat()
                        ),
                        isDay = hour.isDay == 1,
                        icon = "https:${(hour.condition?.icon)?.replace("64x64", "128x128")}",
                        condition = WeatherConditionType.fromCode(hour.condition?.code ?: 0),
                        wind = WindInfo(
                            degree = hour.windDegree ?: 0,
                            direction = hour.windDir ?: "",
                            speed = Wind(
                                kph = (hour.windKph ?: 0.0).toFloat(),
                                mph = (hour.windMph ?: 0.0).toFloat()
                            )
                        ),
                        pressure = AirPressure(
                            hPa = (hour.pressureMb ?: 0.0).toFloat(),
                            mmHg = (hour.pressureIn ?: 0.0).toFloat()
                        ),
                        precipitation = Precipitation(
                            mm = (hour.precipMm ?: 0.0).toFloat(),
                            inch = (hour.precipIn ?: 0.0).toFloat()
                        ),
                        snow = (hour.snowCm ?: 0.0).toFloat(),
                        humidity = hour.humidity ?: 0,
                        cloud = hour.cloud ?: 0,
                        feelsLikeTemp = Temperature(
                            celcius = (hour.feelslikeC ?: 0.0).toFloat(),
                            fahrenheit = (hour.feelslikeF ?: 0.0).toFloat()
                        ),
                        windChill = Temperature(
                            celcius = (hour.windchillC ?: 0.0).toFloat(),
                            fahrenheit = (hour.windchillF ?: 0.0).toFloat()
                        ),
                        dewPoint = Temperature(
                            celcius = (hour.dewpointC ?: 0.0).toFloat(),
                            fahrenheit = (hour.dewpointF ?: 0.0).toFloat()
                        ),
                        willRain = hour.willItRain == 1,
                        chanceOfRain = hour.chanceOfRain ?: 0,
                        willSnow = hour.willItSnow == 1,
                        chanceOfSnow = hour.chanceOfSnow ?: 0,
                        uvIndex = (hour.uv ?: 0.0).toInt(),
                        gust = Wind(
                            kph = (hour.gustKph ?: 0.0).toFloat(),
                            mph = (hour.gustMph ?: 0.0).toFloat()
                        )
                    )
                } ?: emptyList()
            )
        },
        currentInfo = CurrentWeather(
            location = UserLocation(
                country = location?.country ?: "",
                latitude = location?.lat ?: 0.0,
                longitude = location?.lon ?: 0.0,
                state = location?.region ?: "",
                city = location?.name ?: "",
                countryCode = "",
            ),
            localTime = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            ).parse(location?.localtime.toString()) ?: run {
                val calendar = Calendar.getInstance()
                calendar.time
            },
            temp = Temperature(
                celcius = (currentForecast?.tempC ?: 0.0).toFloat(),
                fahrenheit = (currentForecast?.tempF ?: 0.0).toFloat()
            ),
            feelLikeTemp = Temperature(
                celcius = (currentForecast?.feelslikeC ?: 0.0).toFloat(),
                fahrenheit = (currentForecast?.feelslikeF ?: 0.0).toFloat()
            ),
            isDay = currentForecast?.isDay == 1,
            condition = WeatherConditionType.fromCode(currentForecast?.condition?.code ?: 0),
            icon = "https:${(currentForecast?.condition?.icon)?.replace("64x64", "128x128")}",
            wind = WindInfo(
                degree = currentForecast?.windDegree ?: 0,
                direction = currentForecast?.windDir ?: "",
                speed = Wind(
                    kph = (currentForecast?.windKph ?: 0.0).toFloat(),
                    mph = (currentForecast?.windMph ?: 0.0).toFloat()
                )
            ),
            pressure = AirPressure(
                hPa = (currentForecast?.pressureMb ?: 0.0).toFloat(),
                mmHg = (currentForecast?.pressureIn ?: 0.0).toFloat()
            ),
            precipitation = Precipitation(
                mm = (currentForecast?.precipMm ?: 0.0).toFloat(),
                inch = (currentForecast?.precipIn ?: 0.0).toFloat()
            ),
            humidity = currentForecast?.humidity,
            cloud = currentForecast?.cloud,
            uv = currentForecast?.uv,
        )
    )
}

fun ForecastWeatherAltResponseDTO.toDomainModel(unit: UnitType): ForecastWeatherInfo {
    return ForecastWeatherInfo(
        days = days?.map { day->
            ForecastDayInfo(
                localTime = run {
                    try {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day?.datetime ?: "")
                    } catch (e: ParseException) {
                        val calendar = Calendar.getInstance()
                        calendar.time
                    }
                }!!,
                tempStat = TemperatureStat(
                    min = Temperature.fromUnit(unit, day?.tempmin?.toFloat() ?: 0.0f),
                    max = Temperature.fromUnit(unit, day?.tempmax?.toFloat() ?: 0.0f),
                    avg = Temperature.fromUnit(unit, day?.temp?.toFloat() ?: 0.0f)
                ),
                feelsLikeTempStat = TemperatureStat(
                    min = Temperature.fromUnit(unit, day?.feelslikemin?.toFloat() ?: 0.0f),
                    max = Temperature.fromUnit(unit, day?.feelslikemax?.toFloat() ?: 0.0f),
                    avg = Temperature.fromUnit(unit, day?.feelslike?.toFloat() ?: 0.0f)
                ),
                dew = day?.dew ?: 0.0,
                humidity = day?.humidity?.toInt() ?: 0,
                precipitation = Precipitation.fromUnit(unit, day?.precip?.toFloat() ?: 0.0f),
                windGust = Wind.fromUnit(unit, day?.windgust?.toFloat() ?: 0.0f),
                windSpeed = Wind.fromUnit(unit, day?.windspeed?.toFloat() ?: 0.0f),
                windDirection = day?.winddir ?: 0.0,
                pressure = AirPressure.fromUnit(unit, day?.pressure?.toFloat() ?: 0.0f),
                visibility = day?.visibility ?: 0.0,
                uvIndex = day?.uvindex ?: 0,
                astroInfo = AstroInfo(
                    sunrise = run {
                        try {
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("${day?.datetime} ${day?.sunrise}")
                        } catch (e: ParseException) {
                            val calendar = Calendar.getInstance()
                            calendar.time
                        }
                    }!!,
                    sunset = run {
                        try {
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("${day?.datetime} ${day?.sunset}")
                        } catch (e: ParseException) {
                            val calendar = Calendar.getInstance()
                            calendar.time
                        }
                    }!!,
                    moonphase = day?.moonphase.toString(),
                ),
                hourInfo = day?.hours?.map { hour ->
                    ForecastHourInfo(
                        weatherCondition = WeatherConditionType.fromString(hour?.icon ?: ""),
                        uvIndex = hour?.uvindex ?: 0,
                        precipitation = Precipitation.fromUnit(unit, hour?.precip?.toFloat() ?: 0.0f),
                        time = run {
                            try {
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("${day.datetime} ${hour?.datetime}")
                            } catch (e: ParseException) {
                                val calendar = Calendar.getInstance()
                                calendar.time
                            }
                        }!!,
                        temperature = Temperature.fromUnit(unit, hour?.temp?.toFloat() ?: 0.0f),
                        humidity = hour?.humidity ?: 0.0,
                        windGust = Wind.fromUnit(unit, hour?.windgust?.toFloat() ?: 0.0f),
                        windSpeed = Wind.fromUnit(unit, hour?.windspeed?.toFloat() ?: 0.0f),
                        windDirection = hour?.winddir ?: 0.0,
                        feelsLike = Temperature.fromUnit(unit, hour?.feelslike?.toFloat() ?: 0.0f),
                        visibility = hour?.visibility ?: 0.0,
                        weatherDescription = hour?.conditions ?: "",
                        isDay = run {
                            val sunset = run {
                                try{
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("${day.datetime} ${day.sunset}")
                                } catch (e: ParseException) {
                                    val calendar = Calendar.getInstance()
                                    calendar.time
                                }
                            }
                            val sunrise = run {
                                try{
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("${day.datetime} ${day.sunrise}")
                                } catch (e: ParseException) {
                                    val calendar = Calendar.getInstance()
                                    calendar.time
                                }
                            }
                            val currentTime = run {
                                try{
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("${day.datetime} ${hour?.datetime}")
                                } catch (e: ParseException) {
                                    val calendar = Calendar.getInstance()
                                    calendar.time
                                }
                            }
                            (currentTime?.after(sunrise) == true) && currentTime.before(sunset)
                        }
                    )
                } ?: emptyList()
            )
        } ?: emptyList(),
        description = description ?: "",
        location = ForecastLocation(
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            timezone = timezone ?: "",
            address = address ?: ""
        )
    )
}