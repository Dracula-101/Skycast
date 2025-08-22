package com.app.skycast.data.model.forecast_alt


import com.google.gson.annotations.SerializedName

data class CurrentConditions(
    @SerializedName("datetime")
    val datetime: String?,
    @SerializedName("datetimeEpoch")
    val datetimeEpoch: Int?,
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("feelslike")
    val feelslike: Double?,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("dew")
    val dew: Double?,
    @SerializedName("precip")
    val precip: Double?,
    @SerializedName("precipprob")
    val precipprob: Double?,
    @SerializedName("snow")
    val snow: Double?,
    @SerializedName("snowdepth")
    val snowdepth: Double?,
    @SerializedName("windgust")
    val windgust: Double?,
    @SerializedName("windspeed")
    val windspeed: Double?,
    @SerializedName("winddir")
    val winddir: Double?,
    @SerializedName("pressure")
    val pressure: Double?,
    @SerializedName("visibility")
    val visibility: Double?,
    @SerializedName("cloudcover")
    val cloudcover: Double?,
    @SerializedName("solarradiation")
    val solarradiation: Double?,
    @SerializedName("solarenergy")
    val solarenergy: Double?,
    @SerializedName("uvindex")
    val uvindex: Int?,
    @SerializedName("conditions")
    val conditions: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("stations")
    val stations: List<String?>?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunriseEpoch")
    val sunriseEpoch: Int?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("sunsetEpoch")
    val sunsetEpoch: Int?,
    @SerializedName("moonphase")
    val moonphase: Double?
)