package com.app.skycast.domain.model.city

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AutoCompleteCity(
    val name: String,
    val country: String,
    val state: String,
    val latitude: Double,
    val longitude: Double,
    val countryCode: String,
    val timezone: String,
) : Parcelable

fun AutoCompleteCity.toUserLocation(
    isFavourite: Boolean = false
): UserLocation {
    return UserLocation(
        latitude = latitude,
        longitude = longitude,
        city = name,
        country = country,
        countryCode = countryCode,
        state = state,
        isFavourite = isFavourite
    )
}