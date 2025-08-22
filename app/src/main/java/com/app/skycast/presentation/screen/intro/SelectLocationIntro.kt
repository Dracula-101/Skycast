package com.app.skycast.presentation.screen.intro

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.skycast.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SelectLocationIntro(
    modifier: Modifier = Modifier,
    onNavigateToCityLocatorScreen: () -> Unit = {},
    viewModel: SelectLocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val showBottomSheetRationale = remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val activity = context as Activity
        if (isGranted || activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.trySendAction(SelectLocationAction.SetLocation)
        } else {
            if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showBottomSheetRationale.value = true
            }
        }
    }
    var isPlaying by remember { mutableStateOf(true) }
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.earth_location)
    )
    val animationState by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = 1f,
    )
    LaunchedEffect(animationState) {
        if (animationState > 0.5f) {
            isPlaying = false
            delay(1000)
            isPlaying = true
        }
    }

    if(showBottomSheetRationale.value){
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheetRationale.value = false
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Rounded.LocationOn,
                    contentDescription = "Location On",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(80.dp),
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Permission Needed",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "To show accurate weather for your city, please enable location access in your device settings.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(16.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.size(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            val intent =
                                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            context.startActivity(intent)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Row {
                        Text(
                            text = "Go to Settings",
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Or you can select a city manually instead",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }

    Scaffold { innerPadding ->
        Box (
            modifier = Modifier
                .fillMaxSize(),
        ){
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .consumeWindowInsets(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                LottieAnimation(
                    composition = composition,
                    progress = { animationState },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Text(
                    text = "Access Request",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Location",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.size(36.dp))
                Text(
                    "To provide accurate weather updates for your city, we need access to your location. This helps us deliver real-time forecasts tailored just for you.",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.9f),
                )
                Spacer(modifier = Modifier.size(36.dp))
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Row {
                        Text(
                            text = "Allow Access",
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(modifier = Modifier.size(4.dp))

                        Icon(
                            Icons.Rounded.LocationOn,
                            contentDescription = "Arrow Forward",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    "Select a city manually instead",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clickable {
                            onNavigateToCityLocatorScreen()
                        }
                        .drawBehind {
                            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                            drawLine(
                                color = Color.White,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1f,
                                pathEffect = pathEffect
                            )
                        }
                )
                Spacer(modifier = Modifier.size(24.dp))
            }
        }
    }
}