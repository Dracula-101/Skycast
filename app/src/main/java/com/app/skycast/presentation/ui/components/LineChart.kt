package com.app.skycast.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.drawText


@Composable
fun LineChart(
    dataPoints: List<Float>,
    xLabels: List<String>,
    yLabelFormatter: (Float) -> String,
    chartTitle: String,
    textColor: Color,
    modifier: Modifier = Modifier,
    xAxisTextStyle: SpanStyle = MaterialTheme.typography.bodyMedium.toSpanStyle(),
    yAxisTextStyle: SpanStyle = MaterialTheme.typography.bodyMedium.toSpanStyle()
) {
    require(dataPoints.size == xLabels.size) { "Data points and xLabels must have the same size." }

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .drawBehind {
                val xAxisPadding = 32.dp.toPx()
                val yAxisPadding = 16.dp.toPx()

                // Draw x-axis
                drawLine(
                    color = textColor,
                    start = Offset(xAxisPadding, size.height - yAxisPadding),
                    end = Offset(size.width - yAxisPadding, size.height - yAxisPadding),
                    strokeWidth = 8f
                )

                // Draw y-axis
                drawLine(
                    color = textColor,
                    start = Offset(xAxisPadding, yAxisPadding),
                    end = Offset(xAxisPadding, size.height - yAxisPadding),
                    strokeWidth = 8f
                )

                val maxDataPoint = (dataPoints.maxOrNull() ?: 0f)
                val minDataPoint = (dataPoints.minOrNull() ?: 0f)
                val step = 3
                val yAxisRanges = yAxisRanges(minDataPoint, maxDataPoint, step)

                val yStepSize = (size.height - 2 * yAxisPadding) / yAxisRanges.size
                yAxisRanges.forEachIndexed { index, value ->
                    val spreadIndex = index * yStepSize

                    // Draw small line for y-axis labels
                    drawLine(
                        color = textColor,
                        start = Offset(xAxisPadding * 1.2f, size.height - spreadIndex - yAxisPadding),
                        end = Offset(xAxisPadding, size.height - spreadIndex - yAxisPadding),
                        strokeWidth = 4f
                    )

                    val labelText = textMeasurer.measure(
                        buildAnnotatedString {
                            withStyle(yAxisTextStyle.copy(color = textColor)) {
                                append(yLabelFormatter(value))
                            }
                        }
                    )

                    drawText(
                        textLayoutResult = labelText,
                        topLeft = Offset(
                            0f,
                            size.height - yAxisPadding - spreadIndex - labelText.size.height / 2
                        )
                    )
                }

                val xStepSize = (size.width - xAxisPadding - yAxisPadding) / dataPoints.size
                xLabels.forEachIndexed { index, label ->
                    val spreadIndex = index * xStepSize

                    if (index % 5 == 2) {
                        drawLine(
                            color = textColor,
                            start = Offset(spreadIndex + xAxisPadding, size.height - yAxisPadding),
                            end = Offset(spreadIndex + xAxisPadding, size.height - yAxisPadding * 1.3f),
                            strokeWidth = 4f
                        )
                    }

                    val labelText = textMeasurer.measure(
                        buildAnnotatedString {
                            withStyle(xAxisTextStyle.copy(color = textColor)) {
                                append(label)
                            }
                        }
                    )

                    if (index % 5 == 2) {
                        drawText(
                            textLayoutResult = labelText,
                            topLeft = Offset(
                                spreadIndex + xAxisPadding - labelText.size.width / 2,
                                size.height - yAxisPadding / 1.5f
                            )
                        )
                    }
                }

                val dataOffsets = dataPoints.mapIndexed { index, value ->
                    val spreadIndex = index * xStepSize
                    Offset(
                        spreadIndex + xAxisPadding,
                        size.height - yAxisPadding - (value - yAxisRanges.first()) * yStepSize / step
                    )
                }

                dataOffsets.forEachIndexed { index, offset ->
                    if (index < dataOffsets.size - 1) {
                        drawLine(
                            color = textColor,
                            start = offset,
                            end = dataOffsets[index + 1],
                            strokeWidth = 4f
                        )
                    }
                }
            }
    ) {
        Text(
            text = chartTitle,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.TopCenter)
        )
    }
}

private infix fun ClosedFloatingPointRange<Float>.step(step: Int): Sequence<Float> = sequence {
    var current = start
    while (current <= endInclusive) {
        yield(current)
        current += step
    }
}


private fun yAxisRanges(min: Float, max: Float, step: Int): List<Float> {
    val lower = min - step * 2
    val upper = max + step
    val range = mutableListOf<Float>()
    for (i in lower.toInt()..upper.toInt() step step) {
        range.add(i.toFloat())
    }
    return range
}