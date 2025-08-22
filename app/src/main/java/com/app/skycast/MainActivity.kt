package com.app.skycast

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.presentation.screen.app.RootApp
import com.app.skycast.domain.model.AppTheme
import com.app.skycast.presentation.ui.theme.GlobalAppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) {
            mainViewModel.trySendAction(MainAction.ReceiveFirstIntent(intent = intent))
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            val state by mainViewModel.stateFlow.collectAsStateWithLifecycle()
            LaunchedEffect(state.theme) {
                WindowCompat.getInsetsController(window, window.decorView).run {
                    isAppearanceLightStatusBars = state.theme == AppTheme.LIGHT
                }
            }
            GlobalAppTheme(
                darkTheme = state.theme == AppTheme.DARK
            ) { RootApp() }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("App started")
        mainViewModel.trySendAction(MainAction.AppStart)
    }

    override fun onPause() {
        super.onPause()
        Timber.d("App paused")
        mainViewModel.trySendAction(MainAction.AppPause)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("App resumed")
        mainViewModel.trySendAction(MainAction.AppResume)
    }

    override fun onStop() {
        super.onStop()
        Timber.d("App stopped")
        mainViewModel.trySendAction(MainAction.AppStop)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("App destroyed")
        mainViewModel.trySendAction(MainAction.AppDestroy)
    }

}