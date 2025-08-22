package com.app.skycast.presentation.screen.forecast

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.model.forecast_weather.ForecastWeatherInfo
import com.app.skycast.domain.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.parcelize.Parcelize
import java.util.Date
import javax.inject.Inject

const val FORECAST_STATE = ""

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<ForecastState, Unit, ForecastAction>(
    initialState = savedStateHandle[FORECAST_STATE] ?: ForecastState(),
) {

    init {
        combine(
            weatherRepository.currentWeatherInfoFlow(),
            weatherRepository.forecastWeatherAltInfoFlow(),
        ){ currentWeatherInfo, forecastWeatherInfo ->
            if (currentWeatherInfo != null && forecastWeatherInfo != null) {
                val localTime = currentWeatherInfo.localTime
                val otherDayForecast = forecastWeatherInfo.copy(days = forecastWeatherInfo.days.drop(1))
                updateState {
                    it.copy(
                        forecastWeather = otherDayForecast,
                        localTime = localTime,
                    )
                }
            }
        }.launchIn(viewModelScope)
        weatherRepository.fetchCurrentWeatherInfo()
        weatherRepository.fetchForecastAltWeatherInfo()
    }

    override fun handleAction(action: ForecastAction) {
        when(action) {
            else -> {}
        }
    }
}


@Parcelize
data class ForecastState(
    val forecastWeather: ForecastWeatherInfo? = null,
    val localTime: Date? = null,
) : Parcelable

sealed class ForecastAction {



}