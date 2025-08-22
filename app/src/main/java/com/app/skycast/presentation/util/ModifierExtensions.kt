package com.app.skycast.presentation.util

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

fun Modifier.drawDottedLine(color: Color)= drawBehind {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = 1f,
        pathEffect = pathEffect
    )
}

fun Modifier.debugOutline(shape: Shape = RoundedCornerShape(0)) = border(1.dp, Color.Red, shape)

fun Modifier.drawMarker(
    percent: Float,
    color: Color,
    height: Float = 60f,
    width: Float = 12f,
) = drawBehind {
    val x = percent * size.width
    val y = size.height - height
    drawRoundRect(
        color = color,
        topLeft = Offset(x - width / 2, y / 2),
        size = androidx.compose.ui.geometry.Size(width, height),
        cornerRadius = CornerRadius(8f)
    )
}

@Composable
fun Modifier.clickableNoRipple(enabled: Boolean = true, onClick: () -> Unit) = clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick,
    enabled = enabled
)