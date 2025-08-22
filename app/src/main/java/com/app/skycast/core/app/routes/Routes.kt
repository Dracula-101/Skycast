package com.app.skycast.core.app.routes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AppRoute : Parcelable {

    @Parcelize
    data object Splash : AppRoute(), Parcelable {
        override fun toString(): String = "SPLASH"
    }

    @Parcelize
    data object SelectCityGraph : AppRoute(), Parcelable {

        @Parcelize
        data object CityLocator : AppRoute(), Parcelable {
            override fun toString(): String = "CITY_LOCATOR"
        }

        @Parcelize
        data object SelectLocationIntro : AppRoute(), Parcelable {
            override fun toString(): String = "SELECT_LOCATION_INTRO"
        }

        override fun toString(): String = "SELECT_CITY_GRAPH"
    }

    @Parcelize
    data object HomeGraph : AppRoute(), Parcelable {

        override fun toString(): String = "HOME_GRAPH"

        @Parcelize
        data object MainWeatherInfo : AppRoute(), Parcelable {
            override fun toString(): String = "MAIN_WEATHER_INFO"
        }

        @Parcelize
        data object AddLocation : AppRoute(), Parcelable {
            override fun toString(): String = "ADD_LOCATION"
        }

        @Parcelize
        data object Forecast : AppRoute(), Parcelable {
            override fun toString(): String = "FORECAST"
        }

        @Parcelize
        data object News : AppRoute(), Parcelable {
            override fun toString(): String = "NEWS"
        }

        @Parcelize
        data object NewsDetails : AppRoute(), Parcelable {
            override fun toString(): String = "NEWS_DETAILS"
        }

        @Parcelize
        data object AqiInfo : AppRoute(), Parcelable {
            override fun toString(): String = "AQI_INFO"
        }

        @Parcelize
        data object CurrentWeather : AppRoute(), Parcelable {
            override fun toString(): String = "CURRENT_WEATHER"
        }

        @Parcelize
        data object HourlyForecast : AppRoute(), Parcelable {
            override fun toString(): String = "HOURLY_FORECAST"
        }

        @Parcelize
        data object CurrentForecast : AppRoute(), Parcelable {
            override fun toString(): String = "CURRENT_FORECAST"
        }

        @Parcelize
        data object Aqi: AppRoute(), Parcelable {
            override fun toString(): String = "AQI"
        }

    }

    fun index(): Int {
        return when (this) {
            is Splash -> 0
            is SelectCityGraph, SelectCityGraph.CityLocator, SelectCityGraph.SelectLocationIntro -> 1
            is HomeGraph, HomeGraph.MainWeatherInfo -> 2
            is HomeGraph.AddLocation -> 3
            is HomeGraph.Forecast -> 4
            else  -> 5
        }
    }

}

fun String.toAppRoute(): AppRoute {
    return when (this) {
        "SPLASH" -> AppRoute.Splash
        "SELECT_CITY_GRAPH" -> AppRoute.SelectCityGraph
        "CITY_LOCATOR" -> AppRoute.SelectCityGraph.CityLocator
        "SELECT_LOCATION_INTRO" -> AppRoute.SelectCityGraph.SelectLocationIntro
        "HOME_GRAPH" -> AppRoute.HomeGraph
        "MAIN_WEATHER_INFO" -> AppRoute.HomeGraph.MainWeatherInfo
        "ADD_LOCATION" -> AppRoute.HomeGraph.AddLocation
        "FORECAST" -> AppRoute.HomeGraph.Forecast
        "NEWS" -> AppRoute.HomeGraph.News
        "NEWS_DETAILS/{newsArticle}" -> AppRoute.HomeGraph.NewsDetails
        "AQI_INFO" -> AppRoute.HomeGraph.AqiInfo
        "CURRENT_WEATHER" -> AppRoute.HomeGraph.CurrentWeather
        "HOURLY_FORECAST" -> AppRoute.HomeGraph.HourlyForecast
        "CURRENT_FORECAST" -> AppRoute.HomeGraph.CurrentForecast
        "AQI" -> AppRoute.HomeGraph.Aqi
        else -> {
            throw IllegalArgumentException("Invalid route: $this")
        }
    }
}