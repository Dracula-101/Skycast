package com.app.skycast.domain.repository.config

import com.app.skycast.domain.manager.AppConfigManager
import com.app.skycast.domain.model.AppTheme
import kotlinx.coroutines.flow.StateFlow

class AppConfigRepoImpl(
    private val appConfigManager: AppConfigManager
): AppConfigRepository() {

    override fun getAppTheme(): AppTheme {
        return appConfigManager.appTheme
    }

    override fun setAppTheme(appTheme: AppTheme) {
        appConfigManager.appTheme = appTheme
    }

    override fun appThemeFlow(): StateFlow<AppTheme> {
        return appConfigManager.appThemeFlow()
    }
}