package com.app.skycast.data.source.remote.api

import com.app.skycast.data.model.search_city.CityResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface CitySearchRemoteApi {

    @GET("/v2/locations/cities")
    suspend fun getCities(
        @Query("search") query: String,
        @Query("page") limit: Int,
        @Query("size") size: Int = 20
    ): Result<CityResponseDTO>

}