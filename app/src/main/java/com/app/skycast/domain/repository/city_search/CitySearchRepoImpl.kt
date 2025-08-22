package com.app.skycast.domain.repository.city_search

import com.app.skycast.data.source.remote.api.CitySearchRemoteApi
import com.app.skycast.domain.mapper.toDomainModel
import com.app.skycast.domain.model.city.CitySearchResponse
import javax.inject.Inject

class CitySearchRepoImpl @Inject constructor(
    private val citySearchApi: CitySearchRemoteApi
) : CitySearchRepository() {

    override suspend fun getCities(query: String, page: Int): Result<CitySearchResponse> {
        return try {
            val response = citySearchApi.getCities(query, page)
            response.fold(
                onSuccess = { Result.success(it.toDomainModel()) },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}