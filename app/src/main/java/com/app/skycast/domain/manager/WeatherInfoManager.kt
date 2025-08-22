package com.app.skycast.domain.manager

import android.content.SharedPreferences
import com.app.skycast.core.app.base.BaseSharedPrefs
import com.app.skycast.domain.util.bufferedMutableSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class WeatherInfoManager(
    sharedPrefs: SharedPreferences,
): BaseSharedPrefs(sharedPrefs) {

    private val currentWeatherInfoTimestamp = bufferedMutableSharedFlow<Long?>(replay = 1)
    private val forecastWeatherInfoTimestamp = bufferedMutableSharedFlow<Long?>(replay = 1)

    var lastStoredWeatherTimestamp: Long?
        get() = getLong(LAST_STORED_WEATHER_TIMESTAMP)
        set(value) {
            putLong(LAST_STORED_WEATHER_TIMESTAMP, value)
            currentWeatherInfoTimestamp.tryEmit(value)
        }

    val currentWeatherInfoTimestampFlow: StateFlow<Long?>
        get() = currentWeatherInfoTimestamp.stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = lastStoredWeatherTimestamp
        )

    var lastStoredForecastTimestamp: Long?
        get() = getLong(LAST_STORED_FORECAST_TIMESTAMP)
        set(value) {
            putLong(LAST_STORED_FORECAST_TIMESTAMP, value)
            forecastWeatherInfoTimestamp.tryEmit(value)
        }

    val forecastWeatherInfoTimestampFlow: StateFlow<Long?>
        get() = forecastWeatherInfoTimestamp.stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = lastStoredForecastTimestamp
        )

    companion object {
        private const val LAST_STORED_WEATHER_TIMESTAMP = "last_stored_weather_timestamp"
        private const val LAST_STORED_FORECAST_TIMESTAMP = "last_stored_forecast_timestamp"
    }
}