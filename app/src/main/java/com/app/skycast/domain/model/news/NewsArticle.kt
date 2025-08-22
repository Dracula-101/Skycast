package com.app.skycast.domain.model.news

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class NewsArticle (
    val title: String,
    val description: String,
    val url: String,
    val image: String,
    val publishedAt: Date,
    val content: String,
    val source: String,
) : Parcelable {

    companion object {
        val EMPTY = NewsArticle(
            title = "",
            description = "",
            url = "",
            image = "",
            publishedAt = Date(),
            content = "",
            source = "",
        )
    }

}