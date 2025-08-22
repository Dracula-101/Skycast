package com.app.skycast.presentation.screen.splash

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
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.skycast.R

@Composable
fun SplashScreen(
    onNavigateToNextScreen: () -> Unit,
) {
    val dominantColor = remember { mutableStateOf(Color.Black) }
    val vibrantColor = remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val bitmap = context.resources.getDrawable(R.drawable.weather_bg, null).toBitmap()
        val palette = Palette.from(bitmap).generate()
        val dominantSwatch = palette.dominantSwatch
        val vibrantSwatch = palette.vibrantSwatch
        dominantColor.value = dominantSwatch?.rgb?.let { Color(it) } ?: Color.Black
        vibrantColor.value = vibrantSwatch?.rgb?.let { Color(it) } ?: Color.Black
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .background(dominantColor.value),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.weather_bg)
                    .crossfade(500)
                    .build(),
                contentDescription = "Background",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                dominantColor.value.copy(alpha = 0.3f),
                                dominantColor.value.copy(alpha = 0.5f),
                                dominantColor.value.copy(alpha = 0.8f),
                                dominantColor.value,
                            ),
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sun_moon),
                    contentDescription = "Sun Moon",
                    tint = Color.White,
                    modifier = Modifier.size(54.dp),
                )
                Spacer(modifier = Modifier.size(24.dp))
                Text(
                    text = "WELCOME TO",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = context.getString(R.string.app_name),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.size(36.dp))
                Text(
                    "Your go-to weather companion for accurate forecasts and real-time updates, helping you stay prepared no matter the weather",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.9f),
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = innerPadding.calculateBottomPadding() + 8.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(vibrantColor.value.copy(alpha = 0.8f), MaterialTheme.shapes.medium)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        onNavigateToNextScreen()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Row {
                    Text(
                        text = "GET STARTED",
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Arrow Forward",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}