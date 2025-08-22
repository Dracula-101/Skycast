package com.app.skycast.domain.manager

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.LocationManager
import android.os.LocaleList
import android.telephony.TelephonyManager
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.util.bufferedMutableSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Singleton

@Singleton
class AppLocationManager(
    private val locationManager: LocationManager,
    private val telephonyManager: TelephonyManager,
    private val localeList: LocaleList,
    private val geoCoder: Geocoder,
    private val userConfigManager: UserConfigManager
) {

    private val currentLocationFlow = bufferedMutableSharedFlow<UserLocation?>(replay = 1)
    private val currentScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val onCurrentLocationChangeFlow = currentLocationFlow.asSharedFlow()

    init {
        combine(
            userConfigManager.userLocationCityFlow(),
            userConfigManager.userLocationStateFlow(),
            userConfigManager.userLocationCountryFlow(),
            userConfigManager.userLocationCountryCodeFlow(),
            userConfigManager.userLocationLatFlow(),
            userConfigManager.userLocationLangFlow(),
            userConfigManager.hasUserSetLocation(),
        ){
            val city = (it[0] ?: return@combine null).toString()
            val state = it[1] as String?
            val country = it[2] as String?
            val countryCode = it[3] as String?
            val lat = (it[4] ?: return@combine null) as Float
            val lang = (it[5] ?: return@combine null) as Float
            val hasSetLocation = it[6] as Boolean
            if (hasSetLocation) {
                UserLocation(
                    latitude = lat.toDouble(),
                    longitude = lang.toDouble(),
                    city = city,
                    state = state,
                    country = country,
                    countryCode = countryCode,
                )
            } else {
                null
            }
        }.onEach { currentLocationFlow.tryEmit(it) }.launchIn(currentScope)
    }

    fun changeCurrentLocation(userLocation: UserLocation) {
        userConfigManager.userLocationCity = userLocation.city
        userConfigManager.userLocationState = userLocation.state
        userConfigManager.userLocationCountry = userLocation.country
        userConfigManager.userLocationCountryCode = userLocation.countryCode
        userConfigManager.userLocationLat = userLocation.latitude.toFloat()
        userConfigManager.userLocationLang = userLocation.longitude.toFloat()
        userConfigManager.userSetLocation = true
    }

    fun getCurrentLocation(): UserLocation {
        return UserLocation(
            latitude = userConfigManager.userLocationLat?.toDouble() ?: 0.0,
            longitude = userConfigManager.userLocationLang?.toDouble() ?: 0.0,
            city = userConfigManager.userLocationCity ?: "",
            state = userConfigManager.userLocationState,
            country = userConfigManager.userLocationCountry,
            countryCode = userConfigManager.userLocationCountryCode,
        )
    }

    @SuppressLint("MissingPermission")
    fun getNativeLocation(): UserLocation? {
        return try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                ?: return null
            val addressList = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            val city = addressList?.get(0)?.locality ?: return null
            val country = addressList[0]?.countryName
            val state = addressList[0]?.adminArea
            val countryCode = getCountryCode()
            UserLocation(
                latitude = location.latitude,
                longitude = location.longitude,
                city = city,
                state = state,
                country = country,
                countryCode = countryCode,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getCountryCode(): String? {
        val simCountry = detectSIMCountry()
        val networkCountry = detectNetworkCountry()
        val localeCountry = detectLocaleCountry()
        return simCountry ?: networkCountry ?: localeCountry
    }

    private fun detectSIMCountry(): String? {
        return try {
            telephonyManager.simCountryIso
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun detectNetworkCountry(): String? {
        return try {
            telephonyManager.networkCountryIso
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun detectLocaleCountry(): String? {
        return try {
            localeList[0].country
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}