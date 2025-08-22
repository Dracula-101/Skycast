package com.app.skycast.data.model.search_city


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("code")
    val code: String?,
    @SerializedName("continent")
    val continent: Continent?,
    @SerializedName("country")
    val country: Country?,
    @SerializedName("county")
    val county: County?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("postcode")
    val postcode: String?,
    @SerializedName("state")
    val state: State?
)