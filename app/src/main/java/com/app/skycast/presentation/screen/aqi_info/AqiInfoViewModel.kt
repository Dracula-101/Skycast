package com.app.skycast.presentation.screen.aqi_info


import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.model.aqi.ForecastAqi
import com.app.skycast.domain.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

const val AQI_INFO_STATE = ""

@HiltViewModel
class AqiInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<AqiInfoState, Unit, AqiInfoAction>(
    initialState = savedStateHandle[AQI_INFO_STATE] ?: AqiInfoState(),
) {

    init {
        weatherRepository.airQualityInfoFlow()
            .onEach { aqiInfo ->
                updateState { it.copy(forecastAqi = aqiInfo) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            delay(3000)
            if (state.forecastAqi == null) { handleFetchAqiInfo() }
        }
    }

    override fun handleAction(action: AqiInfoAction) {
        when(action) {
            AqiInfoAction.FetchAqiInfo -> handleFetchAqiInfo()
        }
    }

    private fun handleFetchAqiInfo() {
        weatherRepository.fetchAirQualityInfo()
    }

}


@Parcelize
data class AqiInfoState(
    val forecastAqi: ForecastAqi? = null,
) : Parcelable

@Parcelize
sealed class AqiInfoAction : Parcelable {

    @Parcelize
    data object FetchAqiInfo : AqiInfoAction()

}