package com.app.skycast.domain.model.city

data class PopularCity(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
)

fun PopularCity.toUserLocation() = UserLocation(
    latitude = latitude,
    longitude = longitude,
    city = name,
    country = country,
    countryCode = "",
    state = "",
)