package com.app.skycast.domain.repository.city_search

import com.app.skycast.domain.model.city.CitySearchResponse

abstract class CitySearchRepository {

    abstract suspend fun getCities(
        query: String,
        page: Int
    ): Result<CitySearchResponse>

}