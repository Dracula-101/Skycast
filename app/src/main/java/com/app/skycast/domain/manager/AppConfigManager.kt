package com.app.skycast.domain.manager

import android.content.SharedPreferences
import com.app.skycast.core.app.base.BaseSharedPrefs
import com.app.skycast.domain.model.AppTheme
import com.app.skycast.domain.util.bufferedMutableSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AppConfigManager(
    private val sharedPrefs: SharedPreferences,
): BaseSharedPrefs(sharedPrefs){

    private val appThemeFlow = bufferedMutableSharedFlow<AppTheme>()

    var appTheme: AppTheme
        get() = AppTheme.valueOf(sharedPrefs.getString(APP_THEME, AppTheme.DARK.name) ?: "")
        set(value) {
            sharedPrefs.edit().putString(APP_THEME, value.name).apply()
            appThemeFlow.tryEmit(value)
        }

    fun appThemeFlow(): StateFlow<AppTheme> = appThemeFlow.stateIn(
        scope = CoroutineScope(Dispatchers.Unconfined),
        started = SharingStarted.WhileSubscribed(),
        initialValue = appTheme
    )

    companion object {
        const val APP_THEME = "app_theme"
    }

}