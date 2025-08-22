package com.app.skycast.presentation.screen.forecast

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.app.skycast.core.animation.composableWithPushTransitions
import com.app.skycast.core.app.routes.AppRoute

fun NavGraphBuilder.createForecastDestination(
    onBack: () -> Unit,
) {
    composableWithPushTransitions(AppRoute.HomeGraph.Forecast.toString()) {
        ForecastScreen(
            onBack = onBack
        )
    }
}

fun NavController.navigateToForecast(
    navOptions: NavOptions? = null,
) {
    navigate(AppRoute.HomeGraph.Forecast.toString(), navOptions)
}