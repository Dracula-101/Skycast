package com.app.skycast.data.model.aqi


import com.google.gson.annotations.SerializedName

data class CurrentConditions(
    @SerializedName("datetime")
    val datetime: String?,
    @SerializedName("pm1")
    val pm1: Double?,
    @SerializedName("pm2p5")
    val pm2p5: Double?,
    @SerializedName("pm10")
    val pm10: Double?,
    @SerializedName("so2")
    val so2: Double?,
    @SerializedName("no2")
    val no2: Double?,
    @SerializedName("o3")
    val o3: Double?,
    @SerializedName("co")
    val co: Double?,
    @SerializedName("aqius")
    val aqius: Double?,
    @SerializedName("aqieur")
    val aqieur: Double?
)