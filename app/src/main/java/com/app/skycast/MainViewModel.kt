package com.app.skycast

import android.content.Intent
import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.data.source.remote.interceptor.NewsApiInterceptor
import com.app.skycast.data.source.remote.interceptor.WeatherApiInterceptor
import com.app.skycast.data.source.remote.interceptor.WeatherVcApiInterceptor
import com.app.skycast.domain.manager.NetworkManager
import com.app.skycast.domain.model.AppTheme
import com.app.skycast.domain.repository.config.AppConfigRepository
import com.app.skycast.domain.repository.user.config.UserConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject

private const val SPECIAL_CIRCUMSTANCE_KEY = "special-circumstance"

/**
 * A view model that helps launch actions for the [MainActivity].
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val userConfigRepo: UserConfigRepository,
    private val appConfigRepository: AppConfigRepository,
    private val weatherApiInterceptor: WeatherApiInterceptor,
    private val weatherVcApiInterceptor: WeatherVcApiInterceptor,
    private val newsApiInterceptor: NewsApiInterceptor,
) : BaseViewModel<MainState, MainEvent, MainAction>(
    initialState = MainState(
        theme = AppTheme.DARK,
    ),
) {

    init {
        // Config Setup here
        userConfigRepo
            .hasUserSetLocation()
            .onEach { hasUserSetLocation ->
                if (hasUserSetLocation) {
                    val location = userConfigRepo.getUserLocation()
                    weatherApiInterceptor.userLocation = location
                    weatherVcApiInterceptor.userLocation = location
                    newsApiInterceptor.userLocation = location
                }
            }
            .launchIn(viewModelScope)
        userConfigRepo.getUserLocationFlow()
            .onEach { userLocation ->
                weatherApiInterceptor.userLocation = userLocation
                weatherVcApiInterceptor.userLocation = userLocation
                newsApiInterceptor.userLocation = userLocation
            }
            .launchIn(viewModelScope)
        userConfigRepo.userMeasurementUnitFlow()
            .onEach { unitType ->
                weatherVcApiInterceptor.userMeasurement = unitType
            }
            .launchIn(viewModelScope)
        appConfigRepository.appThemeFlow()
            .onEach { appTheme ->
                mutableStateFlow.update { it.copy(theme = appTheme) }
            }
            .launchIn(viewModelScope)
    }

    override fun handleAction(action: MainAction) {
        when (action) {
            is MainAction.Internal.CurrentUserStateChange -> { handleCurrentUserStateChange() }
            is MainAction.Internal.ThemeUpdate -> { handleAppThemeUpdated(action) }
            is MainAction.ReceiveFirstIntent -> { handleFirstIntentReceived(action) }
            is MainAction.ReceiveNewIntent -> { handleNewIntentReceived(action) }
            is MainAction.AppStart -> { handleAppStart() }
            is MainAction.AppPause -> { handleAppPause() }
            is MainAction.AppResume -> { handleAppResume() }
            is MainAction.AppStop -> { handleAppStop() }
            is MainAction.AppDestroy -> { handleAppDestroy() }
        }
    }

    private fun handleCurrentUserStateChange() {
        recreateUiAndGarbageCollect()
    }

    private fun handleAppThemeUpdated(action: MainAction.Internal.ThemeUpdate) {
        mutableStateFlow.update { it.copy(theme = action.theme) }
    }

    private fun handleFirstIntentReceived(action: MainAction.ReceiveFirstIntent) = handleIntent(intent = action.intent, isFirstIntent = true)

    private fun handleNewIntentReceived(action: MainAction.ReceiveNewIntent) = handleIntent(intent = action.intent, isFirstIntent = false)

    private fun handleIntent(
        intent: Intent,
        isFirstIntent: Boolean,
    ) {
        val intentType = intent.type
        val intentAction = intent.action
        val intentData = intent.data
        Timber.i("Intent received: \nType: $intentType\nAction: $intentAction\nData: $intentData, \nType: ${intentData?.javaClass?.simpleName}")

    }

    private fun recreateUiAndGarbageCollect() {
        sendEvent(MainEvent.Recreate)
    }

    private fun handleAppStart() {
        networkManager.registerNetworkCallback()
    }

    private fun handleAppPause() {
    }

    private fun handleAppResume() {
    }

    private fun handleAppStop() {
    }

    private fun handleAppDestroy() {
        networkManager.unregisterNetworkCallback()
    }
}

/**
 * Models state for the [MainActivity].
 */
@Parcelize
data class MainState(
    val theme: AppTheme,
) : Parcelable
/**
 * Models actions for the [MainActivity].
 */
sealed class MainAction {

    /**
     * Receive first Intent by the application.
     */
    data class ReceiveFirstIntent(val intent: Intent) : MainAction()

    /**
     * Receive Intent by the application.
     */
    data class ReceiveNewIntent(val intent: Intent) : MainAction()

    /**
     * Actions for internal use by the ViewModel.
     */
    sealed class Internal : MainAction() {

        /**
         * Indicates a relevant change in the current user state.
         */
        data object CurrentUserStateChange : Internal()
        /**
         * Indicates that the app theme has changed.
         */
        data class ThemeUpdate(
            val theme: AppTheme,
        ) : Internal()
    }

    data object AppStart : MainAction()

    data object AppPause : MainAction()

    data object AppResume : MainAction()

    data object AppStop : MainAction()

    data object AppDestroy : MainAction()

}

/**
 * Represents events that are emitted by the [MainViewModel].
 */
sealed class MainEvent {
    /**
     * Event indicating that the UI should recreate itself.
     */
    data object Recreate : MainEvent()
}