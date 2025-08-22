package com.app.skycast.data.model.aqi_alt


import com.google.gson.annotations.SerializedName

data class AqiResponseDTO(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double?,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String?,
    @SerializedName("elevation")
    val elevation: Int?,
    @SerializedName("current_units")
    val currentUnits: CurrentUnits?,
    @SerializedName("current")
    val current: Current?
)