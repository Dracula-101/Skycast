package com.app.skycast.presentation.screen.home.components.weather

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap


@Composable
fun TemperatureGraph(
    modifier: Modifier = Modifier,
    temperatures: List<TemperatureData>,
    temperatureUnit: String,
    lineColor: Color,
    textColor: Color,
    graphHeight: Dp = 150.dp,
    segmentLength: Dp = 60.dp,
    fontSize: Dp = 40.dp,
) {
    val context = LocalContext.current
    val maxTemp = remember { temperatures.maxBy { it.temperature }.temperature }
    val minTemp = remember { temperatures.minBy { it.temperature }.temperature }
    val graphWidth = remember { (segmentLength.value * (temperatures.size)).dp }
    val graphTemperatures = remember {
        temperatures.let {
            val first = it.firstOrNull()
            val last = it.lastOrNull()
            it.toMutableList().apply {
                first?.let { it1 -> add(0, it1) }
                last?.let { it1 -> add(it1) }
            }
        }
    }
    val bitmaps = remember {
        graphTemperatures.subList(1, graphTemperatures.size - 1).map { temp ->
            ResourcesCompat.getDrawable(
                context.resources,
                temp.weatherIcon,
                null
            )?.toBitmap()?.resize(100) ?: return@remember emptyList()
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        HorizontalScroll(graphWidth) {
            Canvas(
                modifier = Modifier
                    .padding(top = fontSize * 1.25f, bottom = fontSize / 2f)
                    .height(graphHeight)
                    .width(graphWidth)
            ) {
                val path = Path()
                val gradientPath = Path()
                val pointDistance = size.width / (graphTemperatures.size - 1)
                graphTemperatures.forEachIndexed { index, tempData ->
                    val x = index * pointDistance
                    val normalizedY = (1 - (tempData.temperature - minTemp) / (maxTemp - minTemp))
                    val y = normalizedY * (size.height - fontSize.value * 2) + fontSize.value

                    if (index == 0) {
                        path.moveTo(x, y)
                        gradientPath.moveTo(x, y)
                    } else {
                        val prevX = (index - 1) * pointDistance
                        val prevNormalizedY = (1 - (graphTemperatures[index - 1].temperature - minTemp) / (maxTemp - minTemp))
                        val prevY = prevNormalizedY * (size.height - fontSize.value * 2) + fontSize.value

                        val controlX1 = (prevX + x) / 2
                        val controlX2 = (prevX + x) / 2

                        path.cubicTo(controlX1, prevY, controlX2, y, x, y)
                        gradientPath.cubicTo(controlX1, prevY, controlX2, y, x, y)
                    }
                }

                gradientPath.lineTo(size.width, size.height + fontSize.value * 2)
                gradientPath.lineTo(0f, size.height + fontSize.value * 2)
                gradientPath.close()

                drawPath(
                    path = gradientPath.asComposePath(),
                    brush = Brush.verticalGradient(
                        colors = listOf(lineColor.copy(alpha = 0.5f), Color.Transparent),
                        startY = 0f,
                        endY = size.height
                    )
                )

                drawPath(
                    path = path.asComposePath(),
                    color = lineColor,
                    style = Stroke(width = 2.dp.toPx())
                )

                // draw temperature in text
                graphTemperatures.forEachIndexed { index, tempData ->
                    val x = index * pointDistance
                    val normalizedY = (1 - (tempData.temperature - minTemp) / (maxTemp - minTemp))
                    val y = normalizedY * (size.height - fontSize.value * 2) + fontSize.value

                    // text
                    drawIntoCanvas {
                        if(index in 1 until graphTemperatures.size - 1){
                            it.nativeCanvas.drawText(
                                "${tempData.temperature.toInt()}$temperatureUnit",
                                x - fontSize.value,
                                y - 8.dp.toPx(),
                                Paint().apply {
                                    color = textColor.toArgb()
                                    textSize = fontSize.value
                                }
                            )
                            it.nativeCanvas.drawBitmap(
                                bitmaps[index - 1],
                                x - fontSize.value - 2.dp.toPx(),
                                y - fontSize.value * 4f,
                                Paint()
                            )
                        }
                    }
                }
                temperatures.forEachIndexed { index, tempData ->
                    val x = index * pointDistance
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            tempData.time,
                            x - fontSize.value + segmentLength.value * 3,
                            size.height + fontSize.value * 1f,
                            Paint().apply {
                                color = textColor.toArgb()
                                textSize = fontSize.value * 0.8f
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun HorizontalScroll(contentWidth: Dp, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.width(contentWidth)) {
            content()
        }
    }
}


private fun Bitmap.resize(width: Int): Bitmap {
    val aspectRatio = width.toFloat() / this.width
    val height = (this.height * aspectRatio).toInt()
    return Bitmap.createScaledBitmap(this, width, height, false)
}

data class TemperatureData(
    val temperature: Float,
    val time: String,
    val weatherIcon: Int
)