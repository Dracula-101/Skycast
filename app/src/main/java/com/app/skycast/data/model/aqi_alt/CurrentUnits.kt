package com.app.skycast.data.model.aqi_alt


import com.google.gson.annotations.SerializedName

data class CurrentUnits(
    @SerializedName("time")
    val time: String?,
    @SerializedName("interval")
    val interval: String?,
    @SerializedName("european_aqi")
    val europeanAqi: String?,
    @SerializedName("us_aqi")
    val usAqi: String?,
    @SerializedName("pm10")
    val pm10: String?,
    @SerializedName("pm2_5")
    val pm25: String?,
    @SerializedName("carbon_monoxide")
    val carbonMonoxide: String?,
    @SerializedName("nitrogen_dioxide")
    val nitrogenDioxide: String?,
    @SerializedName("sulphur_dioxide")
    val sulphurDioxide: String?,
    @SerializedName("ozone")
    val ozone: String?
)