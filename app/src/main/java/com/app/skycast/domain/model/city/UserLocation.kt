package com.app.skycast.domain.model.city

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val country: String?,
    val countryCode: String?,
    val state: String?,
    val isFavourite: Boolean = false
) : Parcelable {
    companion object {
        val EMPTY: UserLocation = UserLocation(0.0, 0.0, "", null, null, null, false)
    }
}