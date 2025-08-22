package com.app.skycast.data.model.aqi_alt


import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("time")
    val time: String?,
    @SerializedName("interval")
    val interval: Int?,
    @SerializedName("european_aqi")
    val europeanAqi: Int?,
    @SerializedName("us_aqi")
    val usAqi: Int?,
    @SerializedName("pm10")
    val pm10: Double?,
    @SerializedName("pm2_5")
    val pm25: Double?,
    @SerializedName("carbon_monoxide")
    val carbonMonoxide: Int?,
    @SerializedName("nitrogen_dioxide")
    val nitrogenDioxide: Double?,
    @SerializedName("sulphur_dioxide")
    val sulphurDioxide: Double?,
    @SerializedName("ozone")
    val ozone: Int?
)