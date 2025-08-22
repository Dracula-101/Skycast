package com.app.skycast.domain.repository.news

import com.app.skycast.data.source.remote.api.NewsRemoteApi
import com.app.skycast.domain.mapper.toDomainModel
import com.app.skycast.domain.model.news.NewsInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsRepositoryImpl(
    private val newsApi: NewsRemoteApi
): NewsRepository(){

    private val currentScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _newsStateFlow = MutableStateFlow<NewsInfo?>(null)
    private val _isLoadingNewsStateFlow = MutableStateFlow<Boolean>(false)

    override val newsStateFlow: StateFlow<NewsInfo?>
        get() = _newsStateFlow.asStateFlow()

    override val isLoadingNewsStateFlow: StateFlow<Boolean>
        get() = _isLoadingNewsStateFlow.asStateFlow()


    override fun fetchNews() {
        currentScope.launch {
            if (_newsStateFlow.value != null) return@launch
            _isLoadingNewsStateFlow.value = true
            val newsResponse = newsApi.getNews()
            newsResponse.fold(
                onSuccess = {
                    val filteredNews = it.copy(
                        articles = it.articles?.filter { article ->
                            article?.title?.contains("Removed") == false &&
                                    article.description != null &&
                                    article.urlToImage != null
                        }
                    )
                    _newsStateFlow.value = filteredNews.toDomainModel()
                    _isLoadingNewsStateFlow.value = false
                },
                onFailure = {
                    _isLoadingNewsStateFlow.value = false
                }
            )
        }
    }
}