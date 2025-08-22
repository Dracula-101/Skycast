package com.app.skycast.presentation.screen.home.components.drawer

data class DrawerNavigation(
    val navigateToForecast: () -> Unit,
    val navigateToLocations: () -> Unit,
    val navigateToNews: () -> Unit,
    val navigateToAqiInfo: () -> Unit,
    val navigateToPrivacyPolicy: () -> Unit,
    val navigateToSettings: () -> Unit
)