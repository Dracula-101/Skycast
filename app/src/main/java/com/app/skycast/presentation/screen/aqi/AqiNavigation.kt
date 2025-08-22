package com.app.skycast.presentation.screen.aqi

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.app.skycast.core.animation.composableWithPushTransitions
import com.app.skycast.core.app.routes.AppRoute

fun NavGraphBuilder.createAqiDestination(
    onBack: () -> Unit
) {
    composableWithPushTransitions(
        route = AppRoute.HomeGraph.Aqi.toString(),
    ){
        AqiScreen(
            onBack = onBack
        )
    }
}

fun NavController.navigateToAqi(navOptions: NavOptions? = null) {
    navigate(AppRoute.HomeGraph.Aqi.toString(), navOptions)
}