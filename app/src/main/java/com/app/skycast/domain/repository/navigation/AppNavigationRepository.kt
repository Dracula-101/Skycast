package com.app.skycast.domain.repository.navigation

import com.app.skycast.domain.model.NavigationInfo

abstract class AppNavigationRepository {

    abstract fun getLastNavRoute(): NavigationInfo?

    abstract fun setLastNavRoute(route: String)

    abstract fun getStartNavRoute(): NavigationInfo?

    abstract fun setStartNavRoute(route: String)

}