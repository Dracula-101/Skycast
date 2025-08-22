package com.app.skycast.data.source.remote.api

import com.app.skycast.data.model.news.NewsResponseDTO
import retrofit2.http.GET

interface NewsRemoteApi {

    @GET("/v2/everything")
    suspend fun getNews(): Result<NewsResponseDTO>

}