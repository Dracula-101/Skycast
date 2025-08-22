package com.app.skycast.domain.manager

import android.content.SharedPreferences
import com.app.skycast.core.app.base.BaseSharedPrefs
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.domain.util.bufferedMutableSharedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserConfigManager @Inject constructor(
    sharedPrefs: SharedPreferences
) : BaseSharedPrefs(sharedPrefs) {

    private val userLocationStateFlow = bufferedMutableSharedFlow<String?>(replay = 1)
    private val userLocationCityFlow = bufferedMutableSharedFlow<String?>(replay = 1)
    private val userLocationCountryFlow = bufferedMutableSharedFlow<String?>(replay = 1)
    private val userLocationLatFlow = bufferedMutableSharedFlow<Float?>(replay = 1)
    private val userLocationLangFlow = bufferedMutableSharedFlow<Float?>(replay = 1)
    private val userLocationCountryCodeFlow = bufferedMutableSharedFlow<String?>(replay = 1)
    private val userSetLocationFlow = bufferedMutableSharedFlow<Boolean>(replay = 1)
    private val userMeasuringUnit = bufferedMutableSharedFlow<String?>(replay = 1)

    var userLocationState: String?
        get() = getString(USER_LOCATION_STATE)
        set(value) {
            putString(USER_LOCATION_STATE, value)
            userLocationStateFlow.tryEmit(value)
        }

    var userLocationCity: String?
        get() = getString(USER_LOCATION_CITY)
        set(value) {
            putString(USER_LOCATION_CITY, value)
            userLocationCityFlow.tryEmit(value)
        }

    var userLocationCountry: String?
        get() = getString(USER_LOCATION_COUNTRY)
        set(value) {
            putString(USER_LOCATION_COUNTRY, value)
            userLocationCountryFlow.tryEmit(value)
        }

    var userLocationLat: Float?
        get() = getFloat(USER_LOCATION_LAT)
        set(value) {
            putFloat(USER_LOCATION_LAT, value)
            userLocationLatFlow.tryEmit(value)
        }

    var userLocationLang: Float?
        get() = getFloat(USER_LOCATION_LANG)
        set(value) {
            putFloat(USER_LOCATION_LANG, value)
            userLocationLangFlow.tryEmit(value)
        }

    var userLocationCountryCode: String?
        get() = getString(USER_LOCATION_COUNTRY_CODE)
        set(value) {
            putString(USER_LOCATION_COUNTRY_CODE, value)
            userLocationCountryCodeFlow.tryEmit(value)
        }

    var userSetLocation: Boolean
        get() = getBoolean(USER_SET_LOCATION) ?: false
        set(value) {
            putBoolean(USER_SET_LOCATION, value)
            userSetLocationFlow.tryEmit(value)
        }

    var userMeasurementUnit: UnitType
        get() {
            val unit = getString(USER_MEASURING_UNIT)
            return if (unit != null) {
                UnitType.valueOf(unit)
            } else {
                UnitType.METRIC
            }
        }
        set(value) {
            putString(USER_MEASURING_UNIT, value.name)
            userMeasuringUnit.tryEmit(value.name)
        }

    fun getUserSetLocation(): UserLocation? {
        val userLocationCity = userLocationCity ?: return null
        val userLocationState = userLocationState
        val userLocationLat = userLocationLat ?: 0.0f
        val userLocationLang = userLocationLang ?: 0.0f
        val userLocationCountry = userLocationCountry
        val userLocationCountryCode = userLocationCountryCode

        return UserLocation(
            latitude = userLocationLat.toDouble(),
            longitude = userLocationLang.toDouble(),
            city = userLocationCity,
            country = userLocationCountry,
            countryCode = userLocationCountryCode,
            state = userLocationState,
        )
    }

    fun userLocationCityFlow(): Flow<String?> = userLocationCityFlow.asSharedFlow()
    fun userLocationStateFlow(): Flow<String?> = userLocationStateFlow.asSharedFlow()
    fun userLocationCountryFlow(): Flow<String?> = userLocationCountryFlow.asSharedFlow()
    fun userLocationLatFlow(): Flow<Float?> = userLocationLatFlow.asSharedFlow()
    fun userLocationLangFlow(): Flow<Float?> = userLocationLangFlow.asSharedFlow()
    fun userLocationCountryCodeFlow(): Flow<String?> = userLocationCountryCodeFlow.asSharedFlow()
    fun hasUserSetLocation(): Flow<Boolean> = userSetLocationFlow.asSharedFlow()
    fun userMeasuringUnitFlow(): Flow<UnitType?> = userMeasuringUnit.asSharedFlow().map {
        if (it != null) {
            UnitType.valueOf(it)
        } else {
            null
        }
    }

    fun clearUserLocation() {
        removeWithPrefix(USER_LOCATION_STATE)
        removeWithPrefix(USER_LOCATION_CITY)
        removeWithPrefix(USER_LOCATION_COUNTRY)
        removeWithPrefix(USER_LOCATION_LAT)
        removeWithPrefix(USER_LOCATION_LANG)
        removeWithPrefix(USER_LOCATION_COUNTRY_CODE)
        removeWithPrefix(USER_SET_LOCATION)
        removeWithPrefix(USER_MEASURING_UNIT)
    }

    fun clearUserMeasurementUnit() {
        removeWithPrefix(USER_MEASURING_UNIT)
    }

    companion object {
        const val USER_LOCATION_STATE = "user_location_state"
        const val USER_LOCATION_CITY = "user_location_city"
        const val USER_LOCATION_COUNTRY = "user_location_country"
        const val USER_LOCATION_LAT = "user_location_lat"
        const val USER_LOCATION_LANG = "user_location_lang"
        const val USER_LOCATION_COUNTRY_CODE = "user_location_country_code"
        const val USER_SET_LOCATION = "user_set_location"
        const val USER_MEASURING_UNIT = "user_measuring_unit"
    }

}