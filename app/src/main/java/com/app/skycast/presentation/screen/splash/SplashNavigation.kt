package com.app.skycast.presentation.screen.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.app.skycast.core.animation.composableWithStayTransitions
import com.app.skycast.core.app.routes.AppRoute
import com.app.skycast.presentation.screen.intro.navigateToSelectIntroScreen

fun NavGraphBuilder.buildSplashGraph(
    navController: NavHostController,
) {
    composableWithStayTransitions(
        route = AppRoute.Splash.toString()
    ) { backStackEntry ->
        SplashScreen(
            onNavigateToNextScreen = {
                navController.navigateToSelectIntroScreen()
            }
        )
    }
}

fun NavController.navigateToSplashScreen(
    navOptions: NavOptions? = null
) {
    navigate(AppRoute.Splash.toString(), navOptions)
}