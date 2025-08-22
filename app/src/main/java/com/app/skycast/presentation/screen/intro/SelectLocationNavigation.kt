package com.app.skycast.presentation.screen.intro


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.app.skycast.core.animation.composableWithStayTransitions
import com.app.skycast.core.app.routes.AppRoute
import com.app.skycast.presentation.screen.city_locator.createCityLocatorDestination
import com.app.skycast.presentation.screen.city_locator.navigateToCityLocatorScreen

fun NavGraphBuilder.buildSelectCityGraph(navController: NavHostController) {
    navigation(
        startDestination = AppRoute.SelectCityGraph.SelectLocationIntro.toString(),
        route = AppRoute.SelectCityGraph.toString()
    ) {
        createLocationIntroDestination(
            navigateToCityLocatorScreen = {
                navController.navigateToCityLocatorScreen()
            }
        )
        createCityLocatorDestination(
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
}

fun NavGraphBuilder.createLocationIntroDestination(
    navigateToCityLocatorScreen: () -> Unit
) {
    composableWithStayTransitions(
        route = AppRoute.SelectCityGraph.SelectLocationIntro.toString()
    ) { backStackEntry ->
        SelectLocationIntro(
            onNavigateToCityLocatorScreen = navigateToCityLocatorScreen
        )
    }
}

fun NavController.navigateToSelectIntroScreen(
    navOptions: NavOptions? = null
) {
    navigate(AppRoute.SelectCityGraph.SelectLocationIntro.toString(), navOptions)
}

fun NavController.navigateToSelectCityGraph(
    navOptions: NavOptions? = null
) {
    navigate(AppRoute.SelectCityGraph.toString(), navOptions)
}