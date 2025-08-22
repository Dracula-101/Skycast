package com.app.skycast.presentation.screen.news

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.app.skycast.core.animation.composableWithPushTransitions
import com.app.skycast.core.app.routes.AppRoute
import com.app.skycast.domain.model.news.NewsArticle

fun NavGraphBuilder.createNewsDestination(
    onBack: () -> Unit,
    onNavigateToNewsDetails: (NewsArticle) -> Unit
) {
    composableWithPushTransitions(AppRoute.HomeGraph.News.toString()) {
        NewsScreen(
            onBack = onBack,
            onNavigateToNewsDetails = onNavigateToNewsDetails
        )
    }
}

fun NavController.navigateToNewsScreen(navOptions: NavOptions? = null){
    navigate(AppRoute.HomeGraph.News.toString(), navOptions)
}