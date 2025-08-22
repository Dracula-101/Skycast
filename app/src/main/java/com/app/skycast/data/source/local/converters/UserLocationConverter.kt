package com.app.skycast.data.source.local.converters

import com.app.skycast.data.source.local.entity.UserLocationEntity
import com.app.skycast.domain.model.city.UserLocation

fun UserLocationEntity.toUserLocation() = UserLocation(
    city = city,
    country = country,
    latitude = latitude,
    longitude = longitude,
    countryCode = countryCode,
    state = state,
    isFavourite = isFavorite,
)

fun UserLocation.toEntity() = UserLocationEntity(
    id = 0,
    city = city,
    country = country ?: "",
    latitude = latitude,
    longitude = longitude,
    countryCode = countryCode ?: "",
    state = state ?: "",
    isFavorite = isFavourite,
)