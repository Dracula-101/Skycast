package com.app.skycast.domain.mapper

import com.app.skycast.data.model.search_city.CityResponseDTO
import com.app.skycast.domain.model.city.AutoCompleteCity
import com.app.skycast.domain.model.city.CitySearchResponse

fun CityResponseDTO.toDomainModel(): CitySearchResponse {
    return CitySearchResponse(
        cities = this.cities?.map { city ->
            AutoCompleteCity(
                name = city?.name ?: "",
                country = city?.country?.name ?: "",
                state = city?.state?.name ?: "",
                latitude = (city?.latitude ?: "0.0").toDoubleOrNull() ?: 0.0,
                longitude = (city?.longitude ?: "0.0").toDoubleOrNull() ?: 0.0,
                countryCode = city?.country?.code ?: "",
                timezone = buildString {
                    append(city?.continent?.name)
                    if (city?.state?.name?.isNotEmpty() == true) {
                        append("/${city.state.name}")
                    }
                    if (city?.name?.isNotEmpty() == true) {
                        append("/${city.name}")
                    }
                }
            )
        } ?: emptyList(),
        currentPage = this.meta?.currentPage ?: 0,
        totalResults = this.meta?.total ?: 0,
        pageResults = this.meta?.perPage ?: 0,
    )
}
