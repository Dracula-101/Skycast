package com.app.skycast.presentation.screen.current_weather

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.app.skycast.core.animation.composableWithPushTransitions
import com.app.skycast.core.app.routes.AppRoute

fun NavGraphBuilder.createCurrentWeatherDestination(
    onBack: () -> Unit,
) {
    composableWithPushTransitions(
        route = AppRoute.HomeGraph.CurrentWeather.toString(),
    ){
        CurrentWeatherScreen(
            onBack = onBack
        )
    }
}

fun NavController.navigateToCurrentWeather(navOptions: NavOptions? = null){
    navigate(route = AppRoute.HomeGraph.CurrentWeather.toString(), navOptions)
}