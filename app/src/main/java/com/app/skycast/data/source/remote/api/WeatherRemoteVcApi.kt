package com.app.skycast.data.source.remote.api

import com.app.skycast.data.model.aqi.AqiResponseDTO
import com.app.skycast.data.model.forecast_alt.ForecastWeatherAltResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRemoteVcApi {

    @GET("/VisualCrossingWebServices/rest/services/timeline")
    suspend fun getForecastWeather(): Result<ForecastWeatherAltResponseDTO>

    @GET("/VisualCrossingWebServices/rest/services/timeline")
    suspend fun getAqi(
        @Query("elements") elements: String = "datetime,pm1,pm2p5,pm10,o3,no2,so2,co,aqius,aqieur",
    ) : Result<AqiResponseDTO>
}