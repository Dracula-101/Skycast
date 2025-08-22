package com.app.skycast.presentation.screen.city_locator

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.app.skycast.core.animation.composableWithSlideTransitions
import com.app.skycast.core.app.routes.AppRoute

fun NavGraphBuilder.createCityLocatorDestination(
    onNavigateBack: () -> Unit,
) {
    composableWithSlideTransitions(
        route = AppRoute.SelectCityGraph.CityLocator.toString()
    ) { backStackEntry ->
        CityLocatorScreen(
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavHostController.navigateToCityLocatorScreen(navOptions: NavOptions? = null) {
    navigate(AppRoute.SelectCityGraph.CityLocator.toString(), navOptions)
}