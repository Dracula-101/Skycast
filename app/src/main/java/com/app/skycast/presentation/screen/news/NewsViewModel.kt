package com.app.skycast.presentation.screen.news


import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.model.news.NewsInfo
import com.app.skycast.domain.repository.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

const val NEWS_STATE = ""

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository,
) : BaseViewModel<NewsState, Unit, NewsAction>(
    initialState = savedStateHandle[NEWS_STATE] ?: NewsState(),
) {

    init {
        newsRepository.newsStateFlow
            .onEach { newsInfo ->
                updateState { it.copy(newsInfo = newsInfo) }
            }
            .launchIn(viewModelScope)
        trySendAction(NewsAction.LoadNews)
    }

    override fun handleAction(action: NewsAction) {
        when (action) {
            is NewsAction.LoadNews -> handleLoadNews()
        }
    }

    private fun handleLoadNews() {
        newsRepository.fetchNews()
    }
}


@Parcelize
data class NewsState(
    val isLoading: Boolean = true,
    val newsInfo: NewsInfo? = null,
) : Parcelable

@Parcelize
sealed class NewsAction : Parcelable {

    @Parcelize
    data object LoadNews : NewsAction()
}