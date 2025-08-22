package com.app.skycast.presentation.screen.app

import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.app.skycast.core.app.base.BaseViewModel
import com.app.skycast.core.app.routes.AppRoute
import com.app.skycast.domain.repository.navigation.AppNavigationRepository
import com.app.skycast.domain.repository.user.config.UserConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class RootAppViewModel @Inject constructor(
    private val appNavigationRepository: AppNavigationRepository,
    private val userConfigRepository: UserConfigRepository,
) : BaseViewModel<RootAppState, Unit, RootAppAction>(
    initialState = appNavigationRepository.getStartNavRoute()?.route?.toRootAppState() ?: RootAppState.Splash
) {

    init {
        userConfigRepository
            .hasUserSetLocation()
            .onEach { hasUserSetLocation ->
                if (hasUserSetLocation) {
                    updateState { RootAppState.Home }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun handleAction(action: RootAppAction) {
        when (action) {
            is RootAppAction.ChangeTheme -> {
            }

            is RootAppAction.NavigateTo -> {
                val route = action.route
                appNavigationRepository.setLastNavRoute(route.toString())
                if((action.lastRoute?.index() ?: 0) < route.index()) {
                    appNavigationRepository.setStartNavRoute(route.toString())
                }
            }
        }
    }
}

@Parcelize
sealed class RootAppState : Parcelable {

    /**
     * App should show splash nav graph.
     */
    @Parcelize
    data object Splash : RootAppState()

    /**
     * App should show import pdf.
     */
    @Parcelize
    data object SelectCity : RootAppState()

    /**
     * App should show home.
     */
    @Parcelize
    data object Home : RootAppState()

}

fun RootAppState.toAppRoute(): AppRoute {
    return when (this) {
        is RootAppState.Splash -> AppRoute.Splash
        is RootAppState.SelectCity -> AppRoute.SelectCityGraph
        is RootAppState.Home -> AppRoute.HomeGraph
    }
}

fun AppRoute.toRootAppState(): RootAppState {
    return when (this) {
        is AppRoute.Splash -> RootAppState.Splash
        is AppRoute.SelectCityGraph, AppRoute.SelectCityGraph.CityLocator, AppRoute.SelectCityGraph.SelectLocationIntro -> RootAppState.SelectCity
        else -> RootAppState.Home
    }
}

@Parcelize
sealed class RootAppAction : Parcelable {

    @Parcelize
    data object ChangeTheme : RootAppAction()

    @Parcelize
    data class NavigateTo(val route: AppRoute, val lastRoute: AppRoute?) : RootAppAction()

}