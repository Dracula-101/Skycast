package com.app.skycast.presentation.screen.aqi


import android.content.ContentResolver
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.model.aqi.ForecastAqi
import com.app.skycast.domain.model.units.AirQuality
import com.app.skycast.domain.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

const val AQI_STATE = ""

@HiltViewModel
class AqiViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<AqiState, Unit, AqiAction>(
    initialState = savedStateHandle[AQI_STATE] ?: AqiState(),
) {

    init {
        weatherRepository.airQualityInfoFlow()
            .onEach { aqiInfo ->
                updateState { it.copy(currentDayAqi = aqiInfo?.currentDay, forecastAqi = aqiInfo) }
            }
            .launchIn(viewModelScope)
    }

    override fun handleAction(action: AqiAction) { }


}


@Parcelize
data class AqiState(
    val currentDayAqi: AirQuality? = null,
    val forecastAqi: ForecastAqi? = null,
) : Parcelable

sealed class AqiAction {

}