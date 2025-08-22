package com.app.skycast.data.model.current_weather


import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponseDTO(
    @SerializedName("location")
    val location: Location?,
    @SerializedName("current")
    val currentInfo: CurrentInfo?
)