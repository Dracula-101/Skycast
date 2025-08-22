package com.app.skycast.presentation.screen.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.skycast.presentation.ui.components.AppTooltip
import com.app.skycast.presentation.util.clickableNoRipple
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CenterWeatherInfo(
    modifier: Modifier = Modifier,
    animationKey: Int,
    textColor: Color,
    blurColor: Color,
    temperature: Float,
    weatherCondition: String,
    date: Date,
    location: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.75f) }

    val clockHandCount = 64
    val totalAnimationDuration = (clockHandCount - 1) * 25 + 500
    val animationProgress = remember { Animatable(0f) }

    val shouldShowTooltip = remember { mutableStateOf(true) }
    val tooltipPosition = TooltipDefaults.rememberPlainTooltipPositionProvider((-135).dp)
    val tooltipState = rememberBasicTooltipState(isPersistent = true, initialIsVisible = shouldShowTooltip.value)

    val temperatureDigits = remember { temperature.toString().length }
    LaunchedEffect(animationKey) {
        alpha.snapTo(0f)
        scale.snapTo(0.75f)
        animationProgress.snapTo(0f)
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
            )
        }
        launch {
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = totalAnimationDuration,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .sizeIn(maxWidth = 400.dp, maxHeight = 400.dp)
                .fillMaxWidth(0.75f)
                .aspectRatio(1f)
                .blur(36.dp, BlurredEdgeTreatment.Unbounded)
                .background(
                    color = blurColor,
                    shape = CircleShape
                )
        )
        if(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .sizeIn(maxWidth = 400.dp, maxHeight = 400.dp)
                    .fillMaxWidth(0.75f)
                    .aspectRatio(1f),
                color = textColor.copy(alpha = 1f),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
        }
        Box(
            modifier = Modifier
                .sizeIn(maxWidth = 400.dp, maxHeight = 400.dp)
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .drawBehind {
                    // Clock line setup
                    val clockRadius = size.minDimension / 2
                    val clockCenter = Offset(size.width / 2, size.height / 2)
                    val clockLineLength = 12.dp.toPx()
                    val clockLineOffset = -12.dp.toPx()
                    for (i in 0 until clockHandCount) {
                        // Start angle at the top (12 o'clock position) and progress clockwise
                        val angle = Math.toRadians(270.0 + i * (360.0 / clockHandCount))

                        // Calculate dynamic alpha based on animation progress
                        val handStartTime = (i * 25f) / totalAnimationDuration
                        val handEndTime = (i * 25f + 500) / totalAnimationDuration
                        val lineAlpha = when {
                            animationProgress.value < handStartTime -> 0f
                            animationProgress.value > handEndTime -> 1f
                            else -> (animationProgress.value - handStartTime) / (handEndTime - handStartTime)
                        }

                        // Calculate positions for the clock hand
                        val startX =
                            clockCenter.x + (clockRadius - clockLineOffset) * cos(angle).toFloat()
                        val startY =
                            clockCenter.y + (clockRadius - clockLineOffset) * sin(angle).toFloat()
                        val stopX =
                            clockCenter.x + (clockRadius - clockLineOffset - clockLineLength) * cos(
                                angle
                            ).toFloat()
                        val stopY =
                            clockCenter.y + (clockRadius - clockLineOffset - clockLineLength) * sin(
                                angle
                            ).toFloat()

                        // Draw with current alpha
                        drawLine(
                            color = textColor.copy(alpha = lineAlpha),
                            start = Offset(startX, startY),
                            end = Offset(stopX, stopY),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
                .clickableNoRipple {
                    shouldShowTooltip.value = false
                    onClick()
                }
        )
        Column(
            modifier = Modifier
                .sizeIn(maxWidth = 400.dp, maxHeight = 400.dp)
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    alpha = alpha.value
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Box {
                    Text(
                        text = temperature.toString(),
                        fontSize = (120 - (temperatureDigits * 10)).dp.value.sp,
                        modifier = Modifier
                            .height((120 - (temperatureDigits * 10)).dp)
                            .padding(end = 12.dp),
                        fontWeight = FontWeight.Bold,
                        color = textColor.copy(alpha = alpha.value),
                    )
                    Text(
                        text = "°C",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .offset(x = 12.dp, y = (temperatureDigits - 5).dp)
                            .align(Alignment.BottomEnd),
                        color = textColor.copy(alpha = 1f),
                    )
                }
            }
            Text(
                text = weatherCondition,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = textColor.copy(alpha = 1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = SimpleDateFormat(
                    "EEE - dd MMM yyyy",
                    Locale.getDefault()
                ).format(date),
                style = MaterialTheme.typography.bodyLarge,
                color = textColor.copy(alpha = 1f),
                textAlign = TextAlign.Center
            )
            BasicTooltipBox(
                state = tooltipState,
                positionProvider = tooltipPosition,
                tooltip = {
                    AppTooltip(
                        text = "Tap the dial to refresh",
                        backgroundColor = blurColor,
                        textColor = textColor.copy(alpha = 1f),
                        modifier = Modifier.graphicsLayer(alpha = alpha.value),
                    )
                },
                focusable = false
            ) {
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}