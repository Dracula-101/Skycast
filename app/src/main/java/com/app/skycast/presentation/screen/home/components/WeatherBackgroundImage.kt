package com.app.skycast.presentation.screen.home.components

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette


@Composable
fun WeatherBackgroundImage(
    modifier: Modifier = Modifier,
    @DrawableRes weatherImageRes: Int,
) {
    val context = LocalContext.current
    val palette = remember { mutableStateOf<Palette?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(weatherImageRes) {
        val bitmap = BitmapFactory.decodeResource(context.resources, weatherImageRes)
        try {
            Palette.Builder(bitmap).generate { palette.value = it }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading.value = false
        }
    }
    AnimatedVisibility(
        visible = !isLoading.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = modifier) {
            Column {
                Image(
                    painter = painterResource(id = weatherImageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                )
                if (palette.value?.dominantSwatch != null) {
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .offset(y = (-80).dp)
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colorStops = arrayOf(
                                        0.0f to Color.Transparent,
                                        0.6f to Color(palette.value!!.dominantSwatch!!.rgb)
                                    )
                                ),
                            )
                    )
                }
            }
            if (palette.value?.dominantSwatch != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight(0.38f)
                        .fillMaxWidth()
                        .background(
                            Color(palette.value!!.dominantSwatch!!.rgb)
                        )
                )
            }
        }
    }

}