package com.app.skycast.presentation.screen.add_location


import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.data.source.local.converters.toEntity
import com.app.skycast.data.source.local.converters.toUserLocation
import com.app.skycast.data.source.local.dao.UserLocationDao
import com.app.skycast.domain.manager.AppLocationManager
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.city.toUserLocation
import com.app.skycast.domain.repository.city_search.CitySearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

const val ADD_LOCATION_STATE = "add_location"

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userLocationDao: UserLocationDao,
    private val citySearchRepository: CitySearchRepository,
    private val appLocationManager: AppLocationManager
) : BaseViewModel<AddLocationState, Unit, AddLocationAction>(
    initialState = savedStateHandle[ADD_LOCATION_STATE] ?: AddLocationState(),
) {

    init {
        handleAction(AddLocationAction.GetSavedLocations)
        combine(
            appLocationManager.onCurrentLocationChangeFlow,
            userLocationDao.getAllUserLocations()
        ) { currentLocation, savedLocations ->
            updateState { state ->
                state.copy(
                    currentLocation = currentLocation,
                    userSavedLocation = savedLocations.map { it.toUserLocation() }.filter {
                        it.city != currentLocation?.city
                                && it.latitude != currentLocation?.latitude
                                && it.longitude != currentLocation?.longitude
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun handleAction(action: AddLocationAction) {
        when (action) {
            is AddLocationAction.GetSavedLocations -> handleGetSavedLocations()
            is AddLocationAction.SearchLocation -> handleSearchLocation(action.query)
            is AddLocationAction.ShowSaveLocationDialog -> handleShowSaveLocationDialog(action.location)
            is AddLocationAction.HideSaveLocationDialog -> updateState { it.copy(dialogState = null) }
            is AddLocationAction.SaveLocation -> handleSaveLocation(action.location)
            is AddLocationAction.ChangeCurrentLocation -> handleChangeCurrentLocation(action.newLocation)
            is AddLocationAction.ChangeCurrentLocationDialog -> showCurrentLocationDialog(action.newLocation)
            is AddLocationAction.HideChangeCurrentLocationDialog -> updateState { it.copy(dialogState = null) }
            is AddLocationAction.ChangeFavouriteLocation -> handleChangeFavouriteLocation(action.location)
        }
    }

    private fun handleGetSavedLocations() {
        userLocationDao.getAllUserLocations()
            .onEach { locations ->
                val currentLocation = appLocationManager.getCurrentLocation()
                updateState { state ->
                    state.copy(
                        userSavedLocation = locations
                            .filter {
                                it.city != currentLocation.city
                                && it.latitude != currentLocation.latitude
                                && it.longitude != currentLocation.longitude
                            }
                            .map { it.toUserLocation() },
                        currentLocation = currentLocation
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleSearchLocation(query: String) {
        launchInViewModel {
            updateState { it.copy(isLoading = true) }
            val cities = citySearchRepository.getCities(query, state.searchQueryPage).getOrNull()
            updateState {
                it.copy(
                    searchCities = cities?.cities?.map { it.toUserLocation(false) } ?: emptyList(),
                    searchQuery = query,
                    searchQueryPage = (cities?.currentPage ?: 0) + 1,
                    isLoading = false
                )
            }
        }
    }

    private fun handleShowSaveLocationDialog(location: UserLocation) {
        updateState { it.copy(dialogState = AddLocationState.DialogState.ShowSaveLocation(location)) }
    }

    private fun handleSaveLocation(location: UserLocation) {
        launchInViewModel {
            userLocationDao.insertUserLocation(location.toEntity())
            appLocationManager.changeCurrentLocation(location)
            updateState { it.copy(dialogState = null) }
        }
    }

    private fun handleChangeCurrentLocation(location: UserLocation) {
        appLocationManager.changeCurrentLocation(location)
        updateState { it.copy(dialogState = null) }
    }

    private fun showCurrentLocationDialog(newLocation: UserLocation) {
        updateState { it.copy(dialogState = AddLocationState.DialogState.ChangeCurrentLocation(newLocation)) }
    }

    private fun handleChangeFavouriteLocation(location: UserLocation) {
        launchInViewModel {
            userLocationDao.updateFavouriteUserLocation(
                city = location.city,
                isFavourite = !location.isFavourite
            )
        }
    }

}


@Parcelize
data class AddLocationState(
    val isLoading: Boolean = false,
    val userSavedLocation: List<UserLocation>? = null,
    val currentLocation: UserLocation? = null,
    val searchCities: List<UserLocation> = emptyList(),
    val searchQuery: String = "",
    val searchQueryPage: Int = 0,
    val dialogState: DialogState? = null,
) : Parcelable {
    sealed class DialogState : Parcelable {
        @Parcelize
        data class ShowSaveLocation(val location: UserLocation) : DialogState()

        @Parcelize
        data class ChangeCurrentLocation(val newLocation: UserLocation) : DialogState()
    }
}

@Parcelize
sealed class AddLocationAction : Parcelable {

    @Parcelize
    data object GetSavedLocations : AddLocationAction()

    @Parcelize
    data class SearchLocation(val query: String) : AddLocationAction()

    @Parcelize
    data class ShowSaveLocationDialog(val location: UserLocation) : AddLocationAction()

    @Parcelize
    data object HideSaveLocationDialog : AddLocationAction()

    @Parcelize
    data class SaveLocation(val location: UserLocation) : AddLocationAction()

    @Parcelize
    data class ChangeCurrentLocationDialog(val newLocation: UserLocation) : AddLocationAction()

    @Parcelize
    data object HideChangeCurrentLocationDialog : AddLocationAction()

    @Parcelize
    data class ChangeCurrentLocation(val newLocation: UserLocation) : AddLocationAction()

    @Parcelize
    data class ChangeFavouriteLocation(
        val location: UserLocation,
    ): AddLocationAction()

}