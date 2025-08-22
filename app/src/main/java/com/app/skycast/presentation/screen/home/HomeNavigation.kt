package com.app.skycast.presentation.screen.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.app.skycast.core.animation.composableWithStayTransitions
import com.app.skycast.core.app.routes.AppRoute
import com.app.skycast.presentation.screen.add_location.createAddLocationDestination
import com.app.skycast.presentation.screen.add_location.navigateToAddLocation
import com.app.skycast.presentation.screen.aqi.createAqiDestination
import com.app.skycast.presentation.screen.aqi.navigateToAqi
import com.app.skycast.presentation.screen.aqi_info.createAqiInfoDestination
import com.app.skycast.presentation.screen.aqi_info.navigateToAqiInfo
import com.app.skycast.presentation.screen.current_weather.createCurrentWeatherDestination
import com.app.skycast.presentation.screen.current_weather.navigateToCurrentWeather
import com.app.skycast.presentation.screen.forecast.createForecastDestination
import com.app.skycast.presentation.screen.forecast.navigateToForecast
import com.app.skycast.presentation.screen.hourly_forecast.createHourlyForecastDestination
import com.app.skycast.presentation.screen.hourly_forecast.navigateToHourlyForecast
import com.app.skycast.presentation.screen.news.createNewsDestination
import com.app.skycast.presentation.screen.news.navigateToNewsScreen
import com.app.skycast.presentation.screen.news_detail.createNewsDetailsDestination
import com.app.skycast.presentation.screen.news_detail.navigateToNewsDetails

fun NavGraphBuilder.buildHomeGraph(navController: NavController) {
    navigation(
        route = AppRoute.HomeGraph.toString(),
        startDestination = AppRoute.HomeGraph.MainWeatherInfo.toString()
    ){
        createWeatherInfoDestination(
            navController = navController
        )
        createAddLocationDestination(
            onBack = { navController.navigateUp() }
        )
        createForecastDestination(
            onBack = { navController.navigateUp() }
        )
        createNewsDestination(
            onBack = { navController.navigateUp() },
            onNavigateToNewsDetails = { newsArticle ->
                navController.navigateToNewsDetails(newsArticle)
            }
        )
        createNewsDetailsDestination(
            onBack = { navController.navigateUp() },
        )
        createAqiInfoDestination(
            onBack = { navController.navigateUp() }
        )
        createCurrentWeatherDestination(
            onBack = { navController.navigateUp() }
        )
        createHourlyForecastDestination(
            onBack = { navController.navigateUp() }
        )
        createAqiDestination(
            onBack = { navController.navigateUp() }
        )
    }
}

fun NavGraphBuilder.createWeatherInfoDestination(
    navController: NavController
) {
    composableWithStayTransitions(
        route = AppRoute.HomeGraph.MainWeatherInfo.toString()
    ){
        HomeScreen(
            navigateToAddLocations = {
                navController.navigateToAddLocation()
            },
            navigateToForecast = {
                navController.navigateToForecast()
            },
            navigateToNews = {
                navController.navigateToNewsScreen()
            },
            navigateToAqiInfo = {
                navController.navigateToAqiInfo()
            },
            navigateToCurrentWeather = {
                navController.navigateToCurrentWeather()
            },
            navigateToHourlyForecast = {
                navController.navigateToHourlyForecast()
            },
            navigateToAqi = {
                navController.navigateToAqi()
            }
        )
    }
}

fun NavController.navigateToHomeGraph(navOptions: NavOptions? = null) {
    navigate(AppRoute.HomeGraph.toString(), navOptions)
}