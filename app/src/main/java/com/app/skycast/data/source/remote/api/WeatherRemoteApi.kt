package com.app.skycast.data.source.remote.api

import com.app.skycast.data.model.current_weather.CurrentWeatherResponseDTO
import com.app.skycast.data.model.forecast_weather.ForecastWeatherResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRemoteApi {

    @GET("v1/current.json")
    suspend fun getCurrentWeather(): Result<CurrentWeatherResponseDTO>

    @GET("v1/forecast.json")
    suspend fun getForecastWeather(
        @Query("aqi") aqi: String = "yes",
        @Query("days") days: Int = 7
    ): Result<ForecastWeatherResponseDTO>

}