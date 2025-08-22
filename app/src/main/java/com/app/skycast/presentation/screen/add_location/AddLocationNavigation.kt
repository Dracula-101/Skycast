package com.app.skycast.presentation.screen.add_location

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.app.skycast.core.animation.composableWithSlideTransitions
import com.app.skycast.core.app.routes.AppRoute

fun NavGraphBuilder.createAddLocationDestination(
    onBack: () -> Unit
) {
    composableWithSlideTransitions(
        route = AppRoute.HomeGraph.AddLocation.toString()
    ){
        AddLocationScreen(onBack = onBack)
    }
}

fun NavController.navigateToAddLocation() {
    navigate(AppRoute.HomeGraph.AddLocation.toString())
}