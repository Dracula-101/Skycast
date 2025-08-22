package com.app.skycast.presentation.screen.intro

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.domain.manager.AppLocationManager
import com.app.skycast.domain.repository.user.config.UserConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

const val SELECT_LOCATION_STATE = ""

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val locationManager: AppLocationManager,
    private val userConfigRepository: UserConfigRepository,
) : BaseViewModel<SelectLocationState, Unit, SelectLocationAction>(
    initialState = savedStateHandle[SELECT_LOCATION_STATE] ?: SelectLocationState(),
) {
    override fun handleAction(action: SelectLocationAction) {
        when (action) {
            is SelectLocationAction.SetLocation -> {
                val location = locationManager.getNativeLocation()
                location?.let { userConfigRepository.setUserLocation(it) }
            }
        }
    }
}


@Parcelize
data class SelectLocationState(
    val isLoading: Boolean = true
) : Parcelable

@Parcelize
sealed class SelectLocationAction : Parcelable {

    data object SetLocation : SelectLocationAction()

}

