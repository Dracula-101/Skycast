package com.app.skycast.data.model.forecast_weather


import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponseDTO(
    @SerializedName("location")
    val location: Location?,
    @SerializedName("current")
    val currentForecast: CurrentForecast?,
    @SerializedName("forecast")
    val forecast: Forecast?
)