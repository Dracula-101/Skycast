package com.app.skycast.presentation.screen.city_locator

import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.manager.AppLocationManager
import com.app.skycast.domain.model.city.AutoCompleteCity
import com.app.skycast.domain.model.city.PopularCity
import com.app.skycast.domain.model.city.toUserLocation
import com.app.skycast.domain.repository.city_search.CitySearchRepository
import com.app.skycast.domain.repository.user.config.UserConfigRepository
import com.app.skycast.domain.util.popularCities
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CityLocatorViewModel @Inject constructor(
    private val appLocationManager: AppLocationManager,
    private val citySearchRepository: CitySearchRepository,
    private val userConfigRepository: UserConfigRepository
) : BaseViewModel<CityLocatorState, Unit, CityLocatorAction>(CityLocatorState()) {

    init {
        updateState {
            it.copy(popularCities = popularCities)
        }
    }

    override fun handleAction(action: CityLocatorAction) {
        when (action) {
            is CityLocatorAction.SelectSearchCity -> {
                userConfigRepository.setUserLocation(action.city.toUserLocation())
            }
            is CityLocatorAction.SelectPopularCity -> {
                userConfigRepository.setUserLocation(action.city.toUserLocation())
            }
            is CityLocatorAction.SearchQuery -> {
                updateState {
                    it.copy(searchQuery = action.query, currentPage = 1)
                }
            }
            is CityLocatorAction.SearchCity -> {
                launchInViewModel {
                    updateState { it.copy(isLoading = true) }
                    val result = citySearchRepository.getCities(action.query, state.currentPage)
                    result.fold(
                        onSuccess = { response ->
                            updateState { state->
                                state.copy(
                                    searchedCities = response.cities,
                                    currentPage = response.currentPage
                                )
                            }
                        },
                        onFailure = {}
                    )
                    updateState { it.copy(isLoading = false) }
                }
            }
            is CityLocatorAction.LoadNextPage -> {
                launchInViewModel {
                    updateState { it.copy(isLoading = true) }
                    val result = citySearchRepository.getCities(state.searchQuery, state.currentPage + 1)
                    result.fold(
                        onSuccess = { response ->
                            updateState { state->
                                state.copy(
                                    searchedCities = state.searchedCities + response.cities,
                                    currentPage = response.currentPage
                                )
                            }
                        },
                        onFailure = {}
                    )
                    updateState { it.copy(isLoading = false) }
                }
            }
            is CityLocatorAction.ChangeLoadingState -> {
                updateState {
                    it.copy(isLoading = action.isLoading)
                }
            }
        }
    }
}

data class CityLocatorState(
    val searchedCities: List<AutoCompleteCity> = emptyList(),
    val popularCities: List<PopularCity> = emptyList(),
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
)

sealed class CityLocatorAction {
    data class SelectPopularCity(val city: PopularCity) : CityLocatorAction()
    data class SelectSearchCity(val city: AutoCompleteCity) : CityLocatorAction()
    data class SearchQuery(val query: String) : CityLocatorAction()
    data class SearchCity(val query: String) : CityLocatorAction()
    data object LoadNextPage : CityLocatorAction()
    data class ChangeLoadingState(val isLoading: Boolean) : CityLocatorAction()
}