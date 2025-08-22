package com.app.skycast.data.model.aqi


import com.google.gson.annotations.SerializedName

data class AqiResponseDTO(
    @SerializedName("queryCost")
    val queryCost: Int?,
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
    @SerializedName("days")
    val days: List<Day?>?,
    @SerializedName("currentConditions")
    val currentConditions: CurrentConditions?
)