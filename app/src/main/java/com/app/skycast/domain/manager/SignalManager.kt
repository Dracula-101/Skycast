package com.app.skycast.domain.manager

import android.content.SharedPreferences
import com.app.skycast.core.app.base.BaseSharedPrefs
import com.app.skycast.domain.util.bufferedMutableSharedFlow

class SignalManager(
    sharedPrefs: SharedPreferences,
) : BaseSharedPrefs(sharedPrefs) {

    private val _signal = bufferedMutableSharedFlow<Boolean?>(replay = 1)

    var signal: Boolean?
        get() = getBoolean(LAST_STORED_SIGNAL)
        set(value) {
            putBoolean(LAST_STORED_SIGNAL, value)
            _signal.tryEmit(value)
        }

    val signalFlow = _signal

    companion object {
        private const val LAST_STORED_SIGNAL = "last_stored_signal"
    }

}