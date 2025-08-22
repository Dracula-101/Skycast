package com.app.skycast.domain.mapper

import com.app.skycast.data.model.aqi.AqiResponseDTO
import com.app.skycast.domain.model.aqi.DayAqi
import com.app.skycast.domain.model.aqi.ForecastAqi
import com.app.skycast.domain.model.aqi.HourAqi
import com.app.skycast.domain.model.units.AirQuality
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun AqiResponseDTO.toForecastDomainModel(): ForecastAqi {
    return ForecastAqi(
        currentDay = AirQuality(
            value = currentConditions?.aqius ?: 0.0,
            co = currentConditions?.co ?: 0.0,
            no2 = currentConditions?.no2 ?: 0.0,
            o3 = currentConditions?.o3 ?: 0.0,
            so2 = currentConditions?.so2 ?: 0.0,
            pm2_5 = currentConditions?.pm2p5 ?: 0.0,
            pm10 = currentConditions?.pm10 ?: 0.0,
        ),
        forecast = days?.map { day ->
            DayAqi(
                airQuality = AirQuality(
                    value = day?.aqius ?: 0.0,
                    co = day?.co ?: 0.0,
                    no2 = day?.no2 ?: 0.0,
                    o3 = day?.o3 ?: 0.0,
                    so2 = day?.so2 ?: 0.0,
                    pm2_5 = day?.pm2p5 ?: 0.0,
                    pm10 = day?.pm10 ?: 0.0,
                ),
                hourAqi = day?.hours?.map { hour ->
                    HourAqi(
                        date = try {
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse("${day.datetime} ${hour?.datetime}")
                        } catch (e: Exception) {
                            Calendar.getInstance().time
                        }!!,
                        airQuality = AirQuality(
                            value = hour?.aqius ?: 0.0,
                            co = hour?.co ?: 0.0,
                            no2 = hour?.no2 ?: 0.0,
                            o3 = hour?.o3 ?: 0.0,
                            so2 = hour?.so2 ?: 0.0,
                            pm2_5 = hour?.pm2p5 ?: 0.0,
                            pm10 = hour?.pm10 ?: 0.0,
                        )
                    )
                } ?: emptyList(),
                date = try {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day?.datetime ?: "")
                } catch (e: Exception) {
                    Calendar.getInstance().time
                }!!
            )
        } ?: emptyList()
    )
}