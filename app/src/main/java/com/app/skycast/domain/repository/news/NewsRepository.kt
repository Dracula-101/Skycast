package com.app.skycast.domain.repository.news

import com.app.skycast.domain.model.news.NewsInfo
import kotlinx.coroutines.flow.StateFlow

abstract class NewsRepository {

    abstract val newsStateFlow: StateFlow<NewsInfo?>

    abstract val isLoadingNewsStateFlow: StateFlow<Boolean>

    abstract fun fetchNews()

}