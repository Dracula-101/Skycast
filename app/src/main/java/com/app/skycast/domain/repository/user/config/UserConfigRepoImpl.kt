package com.app.skycast.domain.repository.user.config

import com.app.skycast.data.source.local.dao.UserLocationDao
import com.app.skycast.data.source.local.entity.UserLocationEntity
import com.app.skycast.domain.manager.UserConfigManager
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.units.UnitType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserConfigRepoImpl @Inject constructor(
    private val userConfigManager: UserConfigManager,
    private val userLocationDao: UserLocationDao
): UserConfigRepository() {

    private val currentScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getUserLocationFlow(): StateFlow<UserLocation?> {
        return combine(
            userConfigManager.userLocationCityFlow(),
            userConfigManager.userLocationStateFlow(),
            userConfigManager.userLocationCountryFlow(),
            userConfigManager.userLocationCountryCodeFlow(),
            userConfigManager.userLocationLatFlow(),
            userConfigManager.userLocationLangFlow()
        ) { value ->
            val city = value[0].toString()
            val state = value[1].toString()
            val country = value[2].toString()
            val countryCode = value[3].toString()
            val latitude = value[4].toString().toDoubleOrNull()
            val longitude = value[5].toString().toDoubleOrNull()
            UserLocation(
                latitude = latitude ?: 0.0,
                longitude = longitude ?: 0.0,
                city = city,
                country = country,
                countryCode = countryCode,
                state = state
            )
        }.stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = userConfigManager.getUserSetLocation()
        )
    }

    override fun hasUserSetLocation(): StateFlow<Boolean> {
        return userConfigManager
            .hasUserSetLocation()
            .stateIn(
                scope = CoroutineScope(Dispatchers.Unconfined),
                started = SharingStarted.Eagerly,
                initialValue = false
            )
    }

    override fun clearUserLocation() {
        userConfigManager.clearUserLocation()
    }

    override fun getUserMeasurementUnit(): UnitType {
        return userConfigManager.userMeasurementUnit
    }

    override fun setUserMeasurementUnit(unitType: UnitType) {
        userConfigManager.userMeasurementUnit = unitType
    }

    override fun userMeasurementUnitFlow(): StateFlow<UnitType> {
        return userConfigManager.userMeasuringUnitFlow().map {
            it ?: UnitType.METRIC
        }.stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = UnitType.METRIC
        )
    }

    override fun clearUserMeasurementUnit() {
        userConfigManager.clearUserMeasurementUnit()
    }

    override fun getUserLocation(): UserLocation? {
        val userLocationCity = userConfigManager.userLocationCity ?: return null
        val userLocationState = userConfigManager.userLocationState
        val userLocationLat = userConfigManager.userLocationLat ?: 0.0
        val userLocationLang = userConfigManager.userLocationLang ?: 0.0
        val userLocationCountry = userConfigManager.userLocationCountry
        val userLocationCountryCode = userConfigManager.userLocationCountryCode

        return UserLocation(
            latitude = userLocationLat.toDouble(),
            longitude = userLocationLang.toDouble(),
            city = userLocationCity,
            country = userLocationCountry,
            countryCode = userLocationCountryCode,
            state = userLocationState
        )
    }

    override fun setUserLocation(userLocation: UserLocation) {
        userConfigManager.userLocationCity = userLocation.city
        userConfigManager.userLocationState = userLocation.state
        userConfigManager.userLocationCountry = userLocation.country
        userConfigManager.userLocationCountryCode = userLocation.countryCode
        userConfigManager.userLocationLat = userLocation.latitude.toFloat()
        userConfigManager.userLocationLang = userLocation.longitude.toFloat()
        userConfigManager.userSetLocation = true

        currentScope.launch {
            userLocationDao.insertUserLocation(
                UserLocationEntity(
                    id = 0,
                    city = userLocation.city,
                    state = userLocation.state ?: "",
                    country = userLocation.country ?: "",
                    countryCode = userLocation.countryCode ?: "",
                    latitude = userLocation.latitude,
                    longitude = userLocation.longitude,
                    isFavorite = false
                )
            )
        }
    }
}