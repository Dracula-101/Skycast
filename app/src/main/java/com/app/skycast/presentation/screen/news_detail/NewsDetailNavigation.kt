package com.app.skycast.presentation.screen.news_detail

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.app.skycast.core.animation.composableWithPushTransitions
import com.app.skycast.core.app.routes.AppRoute
import com.app.skycast.domain.model.news.NewsArticle
import com.google.gson.Gson

const val NEWS_INFO_ARG = "newsArticle"

fun NavGraphBuilder.createNewsDetailsDestination(
    onBack: () -> Unit,
){
    composableWithPushTransitions(
        route = "${AppRoute.HomeGraph.NewsDetails}/{${NEWS_INFO_ARG}}",
    ){ backStackEntry ->
        val newsArticle = Gson().fromJson(
            backStackEntry.arguments?.getString(NEWS_INFO_ARG),
            NewsArticle::class.java
        )
        NewsDetailScreen(
            onBack = onBack,
            newsArticle = newsArticle
        )
    }
}

fun NavController.navigateToNewsDetails(
    newsArticle: NewsArticle
){
    val encodedNewsArticle = Uri.encode(Gson().toJson(newsArticle))
    navigate("${AppRoute.HomeGraph.NewsDetails}/${encodedNewsArticle}")
}