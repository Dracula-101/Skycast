package com.app.skycast.data.model.current_weather


import com.google.gson.annotations.SerializedName

data class WeatherCondition(
    @SerializedName("text")
    val text: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("code")
    val code: Int?
)