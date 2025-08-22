package com.app.skycast.presentation.screen.home


import android.content.res.AssetManager
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.data.source.local.converters.toUserLocation
import com.app.skycast.data.source.local.dao.UserLocationDao
import com.app.skycast.domain.manager.AppLocationManager
import com.app.skycast.domain.manager.NetworkManager
import com.app.skycast.domain.manager.NetworkStatus
import com.app.skycast.domain.manager.NetworkType
import com.app.skycast.domain.model.AppTheme
import com.app.skycast.domain.model.current_weather.CurrentWeather
import com.app.skycast.domain.model.forecast_weather.ForecastWeather
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.forecast_weather.ForecastWeatherInfo
import com.app.skycast.domain.model.units.AirQuality
import com.app.skycast.domain.repository.config.AppConfigRepository
import com.app.skycast.domain.repository.user.config.UserConfigRepository
import com.app.skycast.domain.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

const val HOME_STATE = ""

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userConfigRepository: UserConfigRepository,
    private val weatherInfoRepository: WeatherRepository,
    private val appConfigRepository: AppConfigRepository,
    private val networkManager: NetworkManager,
    private val assetManager: AssetManager,
    private val appLocationManager: AppLocationManager,
    private val userLocationDao: UserLocationDao,
) : BaseViewModel<HomeState, Unit, HomeAction>(
    initialState = savedStateHandle[HOME_STATE] ?: HomeState(
        userLocationInfo = userConfigRepository.getUserLocation() ?: UserLocation.EMPTY,
    ),
) {

    init {
        networkManager.networkTypeFlow
            .onEach { networkType ->
                updateState { it.copy(networkType = networkType) }
            }
            .launchIn(viewModelScope)
        networkManager.networkStatusFlow
            .onEach { networkStatus ->
                updateState { it.copy(networkStatus = networkStatus) }
                if (networkStatus == NetworkStatus.CONNECTED) {
                    handleAction(HomeAction.FetchWeatherInfo)
                }
            }
            .launchIn(viewModelScope)
        combine(
            weatherInfoRepository.isLoadingCurrentWeatherFlow(),
            weatherInfoRepository.isLoadingForecastWeatherFlow(),
            weatherInfoRepository.isLoadingForecastAltWeatherFlow(),
        ) { isLoadingCurrentWeather, isLoadingForecastWeather, isLoadingForecastAltWeather ->
            isLoadingCurrentWeather || isLoadingForecastWeather || isLoadingForecastAltWeather
        }.onEach { isLoading ->
            updateState { it.copy(isLoading = isLoading) }
        }.launchIn(viewModelScope)


        combine(
            weatherInfoRepository.currentWeatherInfoFlow(),
            weatherInfoRepository.forecastWeatherInfoFlow(),
            weatherInfoRepository.forecastWeatherAltInfoFlow(),
            weatherInfoRepository.airQualityInfoFlow(),
            weatherInfoRepository.lastUpdatedWeatherInfoFlow(),
        ) { currentWeatherInfo, forecastWeatherInfo, forecastAltWeatherInfo, airQuality, lastUpdated ->
            if(currentWeatherInfo != null && forecastWeatherInfo != null) {
                val currentDayForecast = forecastWeatherInfo.copy(
                    forecast = forecastWeatherInfo.forecast?.filter {
                        val forecastDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.date)
                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentWeatherInfo.localTime)
                        forecastDate == currentDate
                    },
                )
                val localTime = currentWeatherInfo.localTime
                // checking which hour is closest to the current time from forecast
                val forecastClosestTime = forecastWeatherInfo.forecast?.firstOrNull()?.updates?.minByOrNull {
                    val localTimeHour = SimpleDateFormat("HH", Locale.getDefault()).format(localTime).toInt()
                    val forecastTimeHour = SimpleDateFormat("HH", Locale.getDefault()).format(it.time).toInt()
                    abs(localTimeHour - forecastTimeHour)
                }
                val isDay = forecastClosestTime?.isDay ?: currentWeatherInfo.isDay
                appConfigRepository.setAppTheme(if (isDay) AppTheme.LIGHT else AppTheme.DARK)
                WeatherInfoStatus.Success(
                    currentWeather = currentWeatherInfo,
                    currentDayForecast = currentDayForecast,
                    otherDaysForecast = forecastWeatherInfo.copy(
                        forecast = forecastWeatherInfo.forecast?.filterNot {
                            val forecastDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.date)
                            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentWeatherInfo.localTime)
                            forecastDate == currentDate
                        }
                    ),
                    airQuality = airQuality?.currentDay,
                    altOtherDaysForecast = forecastAltWeatherInfo,
                    lastUpdated = lastUpdated ?: 0L,
                )
            } else {
                WeatherInfoStatus.Loading
            }
        }.onEach { newState ->
            updateState {
                it.copy(
                    weatherInfoStatus = newState,
                    localTime = if (newState is WeatherInfoStatus.Success) newState.currentWeather.localTime else null
                )
            }
        }.launchIn(viewModelScope)

        appLocationManager.onCurrentLocationChangeFlow
            .onEach {
                handleAction(HomeAction.FetchWeatherInfo)
            }
            .launchIn(viewModelScope)
        combine(
            userConfigRepository.getUserLocationFlow(),
            userLocationDao.getFavouriteUserLocation()
        ){ userLocation, favouriteUserLocation ->
            updateState {
                it.copy(
                    userLocationInfo = userLocation ?: UserLocation.EMPTY,
                    favouriteUserLocations = favouriteUserLocation.map { it.toUserLocation() }
                        .filter { it.city != userLocation?.city }
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun handleAction(action: HomeAction) {
        when (action) {
            HomeAction.FetchWeatherInfo -> handleFetchWeatherInfo()
            HomeAction.FetchCurrentWeatherInfo -> handleFetchCurrentWeatherInfo()
            is HomeAction.ChangeUserLocation -> handleUserLocationChange(action.userLocation)
        }
    }

    private fun handleFetchWeatherInfo() {
        weatherInfoRepository.fetchCurrentWeatherInfo()
        weatherInfoRepository.fetchForecastWeatherInfo()
        weatherInfoRepository.fetchForecastAltWeatherInfo()
        weatherInfoRepository.fetchAirQualityInfo()
    }

    private fun handleFetchCurrentWeatherInfo() {
        updateState { it.copy(isLoading = true) }
        launchInViewModel {
            delay(500)
            weatherInfoRepository.fetchCurrentWeatherInfo()
        }
    }

    private fun handleUserLocationChange(userLocation: UserLocation) {
        appLocationManager.changeCurrentLocation(userLocation)
    }
}


@Parcelize
data class HomeState(
    val weatherInfoStatus: WeatherInfoStatus = WeatherInfoStatus.Loading,
    val userLocationInfo: UserLocation,
    val favouriteUserLocations: List<UserLocation>? = null,
    val networkType: NetworkType = NetworkType.Unknown,
    val networkStatus: NetworkStatus = NetworkStatus.UNKNOWN,
    val isLoading: Boolean = false,
    val localTime: Date? = null,
) : Parcelable

@Parcelize
sealed class HomeAction : Parcelable {

    @Parcelize
    data object FetchWeatherInfo : HomeAction()

    @Parcelize
    data object FetchCurrentWeatherInfo : HomeAction()

    @Parcelize
    data class ChangeUserLocation(val userLocation: UserLocation) : HomeAction()
}

@Parcelize
sealed class WeatherInfoStatus : Parcelable {

    data object Loading : WeatherInfoStatus()

    data class Success(
        val currentWeather: CurrentWeather,
        val currentDayForecast: ForecastWeather,
        val otherDaysForecast: ForecastWeather?,
        val altOtherDaysForecast: ForecastWeatherInfo? = null,
        val airQuality: AirQuality? = null,
        val lastUpdated: Long,
    ) : WeatherInfoStatus()

}