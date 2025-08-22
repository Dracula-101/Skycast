package com.app.skycast.domain.repository.navigation

import com.app.skycast.core.app.routes.toAppRoute
import com.app.skycast.domain.manager.AppNavigationManager
import com.app.skycast.domain.model.NavigationInfo

class AppNavigationRepoImpl(
    private val appNavManager: AppNavigationManager
): AppNavigationRepository() {

    override fun getLastNavRoute(): NavigationInfo? {
        val route = appNavManager.lastRoute ?: return null
        val timestamp = appNavManager.lastRouteTimestamp ?: return null
        return NavigationInfo(route.toAppRoute(), timestamp)
    }

    override fun setLastNavRoute(route: String) {
        appNavManager.lastRoute = route
        appNavManager.lastRouteTimestamp = System.currentTimeMillis()
    }

    override fun getStartNavRoute(): NavigationInfo? {
        val route = appNavManager.startRoute ?: return null
        val timestamp = appNavManager.startRouteTimestamp ?: return null
        return NavigationInfo(route.toAppRoute(), timestamp)
    }

    override fun setStartNavRoute(route: String) {
        appNavManager.startRoute = route
        appNavManager.startRouteTimestamp = System.currentTimeMillis()
    }

}