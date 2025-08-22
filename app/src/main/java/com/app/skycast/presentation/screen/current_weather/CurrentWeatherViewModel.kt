package com.app.skycast.presentation.screen.current_weather


import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.model.current_weather.CurrentWeather
import com.app.skycast.domain.model.forecast_weather.ForecastWeatherInfo
import com.app.skycast.domain.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.parcelize.Parcelize
import java.util.Date
import javax.inject.Inject

const val CURRENT_WEATHER_STATE = ""

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<CurrentWeatherState, Unit, CurrentWeatherAction>(
    initialState = savedStateHandle[CURRENT_WEATHER_STATE] ?: CurrentWeatherState(),
) {

    init {
        combine(
            weatherRepository.currentWeatherInfoFlow(),
            weatherRepository.isLoadingCurrentWeatherFlow(),
            weatherRepository.forecastWeatherAltInfoFlow(),
            weatherRepository.isLoadingForecastAltWeatherFlow(),
            weatherRepository.lastUpdatedWeatherInfoFlow(),
        ) { currentWeather, isLoadingCurrentWeather, forecast, isLoadingForecastWeather, lastUpdated ->
            updateState {
                it.copy(
                    isLoading = isLoadingCurrentWeather || isLoadingForecastWeather,
                    currentWeather = currentWeather,
                    forecast = forecast,
                    lastUpdatedTimestamp = lastUpdated?.let { Date(it) }
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun handleAction(action: CurrentWeatherAction) {
    }
}


@Parcelize
data class CurrentWeatherState(
    val isLoading: Boolean = true,
    val currentWeather: CurrentWeather? = null,
    val forecast: ForecastWeatherInfo? = null,
    val lastUpdatedTimestamp: Date? = null,
) : Parcelable

@Parcelize
sealed class CurrentWeatherAction : Parcelable