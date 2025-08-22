package com.app.skycast.data.model.search_city


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("currentPage")
    val currentPage: Int?,
    @SerializedName("firstPage")
    val firstPage: Int?,
    @SerializedName("lastPage")
    val lastPage: Int?,
    @SerializedName("perPage")
    val perPage: Int?,
    @SerializedName("total")
    val total: Int?
)