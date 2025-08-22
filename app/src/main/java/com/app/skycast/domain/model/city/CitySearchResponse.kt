package com.app.skycast.domain.model.city

data class CitySearchResponse (
    val cities: List<AutoCompleteCity>,
    val currentPage: Int,
    val totalResults: Int,
    val pageResults: Int,
)
