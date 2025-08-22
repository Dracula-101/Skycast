package com.app.skycast.presentation.screen.aqi_info

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.app.skycast.core.animation.composableWithPushTransitions
import com.app.skycast.core.app.routes.AppRoute

fun NavGraphBuilder.createAqiInfoDestination(
    onBack: () -> Unit,
){
    composableWithPushTransitions(
        route = AppRoute.HomeGraph.AqiInfo.toString(),
    ){
        AqiInfoScreen(onBack = onBack)
    }
}

fun NavController.navigateToAqiInfo(){
    navigate(route = AppRoute.HomeGraph.AqiInfo.toString())
}