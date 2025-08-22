package com.app.skycast.presentation.screen.hourly_forecast

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.app.skycast.core.animation.composableWithPushTransitions
import com.app.skycast.core.app.routes.AppRoute

fun NavGraphBuilder.createHourlyForecastDestination(
    onBack: () -> Unit
) {
    composableWithPushTransitions(
        route = AppRoute.HomeGraph.HourlyForecast.toString(),
    ) {
        HourlyForecastScreen(
            onBack = onBack
        )
    }
}

fun NavController.navigateToHourlyForecast(navOptions: NavOptions? = null) {
    navigate(AppRoute.HomeGraph.HourlyForecast.toString(), navOptions)
}