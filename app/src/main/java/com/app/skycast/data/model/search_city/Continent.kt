package com.app.skycast.data.model.search_city


import com.google.gson.annotations.SerializedName

data class Continent(
    @SerializedName("code")
    val code: String?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("nameEs")
    val nameEs: String?,
    @SerializedName("nameFr")
    val nameFr: String?
)