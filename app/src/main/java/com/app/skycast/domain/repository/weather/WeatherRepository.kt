package com.app.skycast.domain.repository.weather

import com.app.skycast.domain.model.aqi.ForecastAqi
import com.app.skycast.domain.model.current_weather.CurrentWeather
import com.app.skycast.domain.model.forecast_weather.ForecastWeather
import com.app.skycast.domain.model.forecast_weather.ForecastWeatherInfo
import kotlinx.coroutines.flow.StateFlow

abstract class WeatherRepository {

    abstract fun currentWeatherInfoFlow(): StateFlow<CurrentWeather?>

    abstract fun isLoadingCurrentWeatherFlow(): StateFlow<Boolean>

    abstract fun fetchCurrentWeatherInfo()

    abstract fun forecastWeatherInfoFlow(): StateFlow<ForecastWeather?>

    abstract fun isLoadingForecastWeatherFlow(): StateFlow<Boolean>

    abstract fun fetchForecastWeatherInfo()

    abstract fun forecastWeatherAltInfoFlow(): StateFlow<ForecastWeatherInfo?>

    abstract fun isLoadingForecastAltWeatherFlow(): StateFlow<Boolean>

    abstract fun fetchForecastAltWeatherInfo()

    abstract fun airQualityInfoFlow(): StateFlow<ForecastAqi?>

    abstract fun fetchAirQualityInfo()

    abstract fun lastUpdatedWeatherInfoFlow(): StateFlow<Long?>

}