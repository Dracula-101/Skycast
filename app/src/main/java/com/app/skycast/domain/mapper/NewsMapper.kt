package com.app.skycast.domain.mapper

import com.app.skycast.data.model.news.NewsResponseDTO
import com.app.skycast.domain.model.news.NewsArticle
import com.app.skycast.domain.model.news.NewsInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun NewsResponseDTO.toDomainModel(): NewsInfo {
    return NewsInfo(
        articles = articles?.map {
            NewsArticle(
                title = it?.title ?: "",
                description = it?.description ?: "",
                url = it?.url ?: "",
                image = it?.urlToImage ?: "",
                publishedAt = run {
                    try{
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(it?.publishedAt ?: "")
                    } catch (e: Exception) {
                        Calendar.getInstance().time
                    }
                }!!,
                content = it?.content ?: "",
                source = it?.source?.name ?: ""
            )
        } ?: emptyList()
    )
}