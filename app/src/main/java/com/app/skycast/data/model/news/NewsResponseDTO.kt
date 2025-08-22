package com.app.skycast.data.model.news


import com.google.gson.annotations.SerializedName

data class NewsResponseDTO(
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?,
    @SerializedName("articles")
    val articles: List<Article?>?
)