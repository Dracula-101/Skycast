package com.app.skycast.domain.repository.weather

import com.app.skycast.data.source.remote.api.WeatherRemoteApi
import com.app.skycast.data.source.remote.api.WeatherRemoteVcApi
import com.app.skycast.domain.manager.UserConfigManager
import com.app.skycast.domain.manager.WeatherInfoManager
import com.app.skycast.domain.mapper.toDomainModel
import com.app.skycast.domain.mapper.toForecastDomainModel
import com.app.skycast.domain.model.aqi.ForecastAqi
import com.app.skycast.domain.model.current_weather.CurrentWeather
import com.app.skycast.domain.model.forecast_weather.ForecastWeather
import com.app.skycast.domain.model.forecast_weather.ForecastWeatherInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherRepoImpl @Inject constructor(
    private val weatherApi: WeatherRemoteApi,
    private val weatherVcApi: WeatherRemoteVcApi,
    private val weatherInfoManager: WeatherInfoManager,
    private val userConfigManager: UserConfigManager
) : WeatherRepository() {

    private val currentScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    private val isLoadingCurrentWeather = MutableStateFlow(false)
    private val currentWeatherInfo = MutableStateFlow<CurrentWeather?>(null)
    private val isLoadingForecastWeather = MutableStateFlow(false)
    private val forecastWeatherInfo = MutableStateFlow<ForecastWeather?>(null)
    private val forecastWeatherAltInfo = MutableStateFlow<ForecastWeatherInfo?>(null)
    private val isLoadingForecastAltWeather = MutableStateFlow(false)
    private val forecastAqiFlow = MutableStateFlow<ForecastAqi?>(null)

    private val currentWeatherInfoUpdateTimestamp =
        weatherInfoManager.currentWeatherInfoTimestampFlow
            .stateIn(
                currentScope,
                SharingStarted.Eagerly,
                weatherInfoManager.lastStoredWeatherTimestamp
            )
    private val forecastWeatherInfoUpdateTimestamp =
        weatherInfoManager.forecastWeatherInfoTimestampFlow
            .stateIn(
                currentScope,
                SharingStarted.Eagerly,
                weatherInfoManager.lastStoredWeatherTimestamp
            )


    override fun currentWeatherInfoFlow(): StateFlow<CurrentWeather?> {
        return currentWeatherInfo.asStateFlow()
    }

    override fun isLoadingCurrentWeatherFlow(): StateFlow<Boolean> {
        return isLoadingCurrentWeather.asStateFlow()
    }

    override fun fetchCurrentWeatherInfo() {
        currentScope.launch {
            isLoadingCurrentWeather.value = true
            val currentWeather = weatherApi.getCurrentWeather()
            currentWeather.fold(
                onSuccess = {
                    isLoadingCurrentWeather.value = false
                    currentWeatherInfo.value = it.toDomainModel()
                    weatherInfoManager.lastStoredWeatherTimestamp = System.currentTimeMillis()
                },
                onFailure = {
                    it.printStackTrace()
                }
            )
        }
    }

    override fun forecastWeatherInfoFlow(): StateFlow<ForecastWeather?> {
        return forecastWeatherInfo.asStateFlow()
    }

    override fun isLoadingForecastWeatherFlow(): StateFlow<Boolean> {
        return isLoadingForecastWeather.asStateFlow()
    }

    override fun fetchForecastWeatherInfo() {
        currentScope.launch {
            isLoadingForecastWeather.value = true
            val forecastWeather = weatherApi.getForecastWeather(days = 10)
            forecastWeather.fold(
                onSuccess = {
                    isLoadingForecastWeather.value = false
                    forecastWeatherInfo.value = it.toDomainModel()
                    weatherInfoManager.lastStoredForecastTimestamp = System.currentTimeMillis()
                },
                onFailure = {
                    it.printStackTrace()
                }
            )
        }
    }

    override fun forecastWeatherAltInfoFlow(): StateFlow<ForecastWeatherInfo?> {
        return forecastWeatherAltInfo.asStateFlow()
    }

    override fun isLoadingForecastAltWeatherFlow(): StateFlow<Boolean> {
        return isLoadingForecastAltWeather.asStateFlow()
    }

    override fun fetchForecastAltWeatherInfo() {
        currentScope.launch {
            isLoadingForecastAltWeather.value = true
            val forecastWeather = weatherVcApi.getForecastWeather()
            forecastWeather.fold(
                onSuccess = {
                    isLoadingForecastAltWeather.value = false
                    forecastWeatherAltInfo.value = it.toDomainModel(userConfigManager.userMeasurementUnit)
                    weatherInfoManager.lastStoredForecastTimestamp = System.currentTimeMillis()
                },
                onFailure = {
                    it.printStackTrace()
                }
            )
        }
    }

    override fun airQualityInfoFlow(): StateFlow<ForecastAqi?> {
        return forecastAqiFlow.asStateFlow()
    }

    override fun fetchAirQualityInfo() {
        currentScope.launch {
            val airQuality = weatherVcApi.getAqi()
            airQuality.fold(
                onSuccess = { response ->
                    val filteredResponse = response.copy(
                        days = response.days?.filter { day -> day?.hours?.any { it?.aqius != null } ?: false } ?: emptyList()
                    )
                    forecastAqiFlow.value = filteredResponse.toForecastDomainModel()
                },
                onFailure = {
                    it.printStackTrace()
                }
            )
        }
    }


    override fun lastUpdatedWeatherInfoFlow(): StateFlow<Long?> {
        return combine(
            currentWeatherInfoUpdateTimestamp,
            forecastWeatherInfoUpdateTimestamp
        ) { current, forecast ->
            current ?: forecast ?: 0
        }.stateIn(
            currentScope,
            SharingStarted.Eagerly,
            null
        )
    }
}