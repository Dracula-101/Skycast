package com.app.skycast.domain.model

import com.app.skycast.core.app.routes.AppRoute

data class NavigationInfo(
    val route: AppRoute,
    val timestamp: Long,
)

