package com.app.skycast.presentation.screen.hourly_forecast


import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.model.forecast_weather.ForecastDayInfo
import com.app.skycast.domain.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.parcelize.Parcelize
import java.util.Date
import javax.inject.Inject

const val HOURLY_FORECAST_STATE = ""

@HiltViewModel
class HourlyForecastViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<HourlyForecastState, Unit, HourlyForecastAction>(
    initialState = savedStateHandle[HOURLY_FORECAST_STATE] ?: HourlyForecastState(),
) {

    init {
        combine(
            weatherRepository.currentWeatherInfoFlow(),
            weatherRepository.forecastWeatherAltInfoFlow()
        ) { currentWeatherInfo, forecastWeatherInfo ->
            updateState {
                it.copy(
                    hourlyForecastInfo = forecastWeatherInfo?.days?.firstOrNull(),
                    localTime = currentWeatherInfo?.localTime
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun handleAction(action: HourlyForecastAction) { }
}


@Parcelize
data class HourlyForecastState(
    val hourlyForecastInfo: ForecastDayInfo? = null,
    val localTime: Date? = null,
) : Parcelable {

    sealed class HourlyForecastDialogState : Parcelable {}

}

sealed class HourlyForecastAction {
}