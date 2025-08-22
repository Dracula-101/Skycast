package com.app.skycast.domain.model.news

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsInfo(
    val articles: List<NewsArticle>,
) : Parcelable