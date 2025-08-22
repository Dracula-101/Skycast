package com.app.skycast.data.model.search_city


import com.google.gson.annotations.SerializedName

data class CityResponseDTO(
    @SerializedName("cities")
    val cities: List<City?>?,
    @SerializedName("meta")
    val meta: Meta?
)