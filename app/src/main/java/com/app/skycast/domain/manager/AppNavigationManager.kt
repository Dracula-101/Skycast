package com.app.skycast.domain.manager

import android.content.SharedPreferences
import com.app.skycast.core.app.base.BaseSharedPrefs
import com.app.skycast.domain.util.bufferedMutableSharedFlow

class AppNavigationManager(
    sharedPrefs: SharedPreferences
) : BaseSharedPrefs(sharedPrefs) {

    private val currentRouteFlow = bufferedMutableSharedFlow<String?>(replay = 1)
    private val currentRouteTimestampFlow = bufferedMutableSharedFlow<Long?>(replay = 1)

    private val lastRouteFlow = bufferedMutableSharedFlow<String?>(replay = 1)
    private val lastRouteTimestampFlow = bufferedMutableSharedFlow<Long?>(replay = 1)

    private val startRouteFlow = bufferedMutableSharedFlow<String?>(replay = 1)
    private val startRouteTimestampFlow = bufferedMutableSharedFlow<Long?>(replay = 1)

    var currentRoute: String?
        get() = getString(CURRENT_ROUTE)
        set(value) {
            putString(CURRENT_ROUTE, value)
            currentRouteFlow.tryEmit(value)
        }

    var currentRouteTimestamp: Long?
        get() = getLong(CURRENT_ROUTE_TIMESTAMP)
        set(value) {
            putLong(CURRENT_ROUTE_TIMESTAMP, value)
            currentRouteTimestampFlow.tryEmit(value)
        }

    var lastRoute: String?
        get() = getString(LAST_ROUTE)
        set(value) {
            putString(LAST_ROUTE, value)
            lastRouteFlow.tryEmit(value)
        }

    var lastRouteTimestamp: Long?
        get() = getLong(LAST_ROUTE_TIMESTAMP)
        set(value) {
            putLong(LAST_ROUTE_TIMESTAMP, value)
            lastRouteTimestampFlow.tryEmit(value)
        }

    var startRoute: String?
        get() = getString(START_ROUTE)
        set(value) {
            putString(START_ROUTE, value)
            startRouteFlow.tryEmit(value)
        }

    var startRouteTimestamp: Long?
        get() = getLong(START_ROUTE_TIMESTAMP)
        set(value) {
            putLong(START_ROUTE_TIMESTAMP, value)
            startRouteTimestampFlow.tryEmit(value)
        }

    fun clearAll() {
        removeWithPrefix(CURRENT_ROUTE)
        removeWithPrefix(CURRENT_ROUTE_TIMESTAMP)
        removeWithPrefix(LAST_ROUTE)
        removeWithPrefix(LAST_ROUTE_TIMESTAMP)
        removeWithPrefix(START_ROUTE)
        removeWithPrefix(START_ROUTE_TIMESTAMP)
    }


    companion object {
        private const val CURRENT_ROUTE = "current_route"
        private const val CURRENT_ROUTE_TIMESTAMP = "current_route_timestamp"

        private const val LAST_ROUTE = "last_route"
        private const val LAST_ROUTE_TIMESTAMP = "last_route_timestamp"

        private const val START_ROUTE = "start_route"
        private const val START_ROUTE_TIMESTAMP = "start_route_timestamp"

    }

}