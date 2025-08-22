package com.app.skycast.core.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun FadeAnimation(
    animationKey: Any,
    modifier: Modifier = Modifier,
    duration: Int = 500,
    content: @Composable () -> Unit
) {
    val fadeAnimation = remember { Animatable(0f) }

    LaunchedEffect(animationKey) {
        fadeAnimation.snapTo(0f)
        fadeAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(duration, easing = FastOutSlowInEasing)
        )
    }
    Box(
        modifier = modifier.graphicsLayer(alpha = fadeAnimation.value),
    ) { content() }
}