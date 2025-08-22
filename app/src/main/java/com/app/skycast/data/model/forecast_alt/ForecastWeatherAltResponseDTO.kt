package com.app.skycast.data.model.forecast_alt


import com.google.gson.annotations.SerializedName

data class ForecastWeatherAltResponseDTO(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("resolvedAddress")
    val resolvedAddress: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("tzoffset")
    val tzoffset: Double?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("days")
    val days: List<Day?>?,
    @SerializedName("currentConditions")
    val currentConditions: CurrentConditions?
)