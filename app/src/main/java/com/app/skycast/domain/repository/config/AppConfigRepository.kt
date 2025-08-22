package com.app.skycast.domain.repository.config

import com.app.skycast.domain.model.AppTheme
import kotlinx.coroutines.flow.StateFlow

abstract class AppConfigRepository {

    abstract fun getAppTheme() : AppTheme

    abstract fun setAppTheme(appTheme: AppTheme)

    abstract fun appThemeFlow() : StateFlow<AppTheme>

}