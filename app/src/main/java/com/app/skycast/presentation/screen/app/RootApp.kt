package com.app.skycast.presentation.screen.app

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.app.skycast.core.animation.toEnterTransition
import com.app.skycast.core.animation.toExitTransition
import com.app.skycast.core.app.routes.AppRoute
import com.app.skycast.core.app.routes.toAppRoute
import com.app.skycast.presentation.screen.home.buildHomeGraph
import com.app.skycast.presentation.screen.home.navigateToHomeGraph
import com.app.skycast.presentation.screen.intro.buildSelectCityGraph
import com.app.skycast.presentation.screen.intro.navigateToSelectCityGraph
import com.app.skycast.presentation.screen.splash.buildSplashGraph
import com.app.skycast.presentation.screen.splash.navigateToSplashScreen
import java.util.concurrent.atomic.AtomicReference

@Composable
fun RootApp(
    viewModel: RootAppViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val previousStateReference = remember { AtomicReference(state) }

    DisposableEffect(Unit) {
        val navListener =  NavController.OnDestinationChangedListener { navController, destination, _ ->
            val destinationRoute = destination.route ?: return@OnDestinationChangedListener
            val previousRoute = navController.previousBackStackEntry?.destination?.route
            viewModel.trySendAction(
                RootAppAction.NavigateTo(
                    destinationRoute.toAppRoute(),
                    previousRoute?.toAppRoute()
                )
            )
        }
        navController.addOnDestinationChangedListener(navListener)
        onDispose {
            navController.removeOnDestinationChangedListener(navListener)
        }
    }

    NavHost(
        navController = navController,
        startDestination = state.toAppRoute().toString(),
        enterTransition = { toEnterTransition()(this) },
        exitTransition = { toExitTransition()(this) },
        popEnterTransition = { toEnterTransition()(this) },
        popExitTransition = { toExitTransition()(this) },
    ) {
        buildSplashGraph(navController)
        buildSelectCityGraph(navController)
        buildHomeGraph(navController)
    }
    val targetRoute = when (state) {
        RootAppState.SelectCity -> AppRoute.SelectCityGraph
        RootAppState.Splash -> AppRoute.Splash
        RootAppState.Home -> AppRoute.HomeGraph
    }.toString()
    val currentRoute = navController.currentDestination?.rootLevelRoute()
    if (currentRoute == targetRoute && previousStateReference.get() == state) {
        previousStateReference.set(state)
        return
    }
    previousStateReference.set(state)
    (LocalContext.current as? Activity)?.currentFocus?.clearFocus()
    val rootNavOptions = navOptions {
        // When changing root navigation state, pop everything else off the back stack:
        popUpTo(navController.graph.id) {
            inclusive = false
            saveState = false
        }
        launchSingleTop = true
        restoreState = false
    }
    LaunchedEffect(state) {
        when (state) {
            RootAppState.SelectCity -> navController.navigateToSelectCityGraph(rootNavOptions)
            RootAppState.Splash -> navController.navigateToSplashScreen(rootNavOptions)
            RootAppState.Home -> navController.navigateToHomeGraph(rootNavOptions)
        }
    }
}

fun NavDestination?.rootLevelRoute(): String? {
    if (this == null) {
        return null
    }
    if (parent?.route == null) {
        return route
    }
    return parent.rootLevelRoute()
}