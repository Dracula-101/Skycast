package com.app.skycast.presentation.screen.hourly_forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.R
import com.app.skycast.core.animation.FadeAnimation
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.presentation.ui.components.AppBar
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HourlyForecastScreen(
    modifier: Modifier = Modifier,
    viewModel: HourlyForecastViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val unit = UnitType.METRIC
    val textColor = MaterialTheme.colorScheme.onSurface
    val temperatureScrollState = rememberScrollState()
    val density = LocalDensity.current
    val currentTimeTemperatureIndex = remember { mutableStateOf(0) }
    val temperatures = remember { state.hourlyForecastInfo?.hourInfo?.map { it.temperature.getValue(unit) } }
    LaunchedEffect(
        key1 = state.localTime,
        key2 = state.hourlyForecastInfo,
    ) {
        // scroll to current time
        val currentTime = state.localTime ?: return@LaunchedEffect
        val hourlyForecast = state.hourlyForecastInfo ?: return@LaunchedEffect
        val currentHour = SimpleDateFormat("H", Locale.getDefault()).format(currentTime).toInt()
        val currentHourIndex = hourlyForecast.hourInfo.indexOfFirst { SimpleDateFormat("H", Locale.getDefault()).format(it.time).toInt() == currentHour }
        currentTimeTemperatureIndex.value = currentHourIndex
    }

    LaunchedEffect(currentTimeTemperatureIndex.value) {
        val pixelOffset = density.run {
            70.dp.toPx() * currentTimeTemperatureIndex.value
        }
        temperatureScrollState.animateScrollTo(pixelOffset.toInt())
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "Hourly Forecast",
                onBack = onBack,
            )
        }
    ) { innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding),
        ){
            Row (
                modifier = Modifier.horizontalScroll(temperatureScrollState),
            ) {
                val minTemperature = remember { temperatures?.minOrNull() ?: 0f }
                val maxTemperature = remember { temperatures?.maxOrNull() ?: 0f }
                state.hourlyForecastInfo?.hourInfo?.forEachIndexed { index, hourlyForecast ->
                    Box(
                        modifier = Modifier
                            .then(
                                if (index == currentTimeTemperatureIndex.value) {
                                    Modifier.border(1.dp, MaterialTheme.colorScheme.primary)
                                } else {
                                    Modifier
                                }
                            )
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .fillMaxHeight(0.4f)
                            .width(70.dp)
                            .clickable {
                                currentTimeTemperatureIndex.value = index
                            },
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(id = hourlyForecast.weatherCondition.getDrawableIcon(hourlyForecast.isDay)),
                                contentDescription = null,
                            )
                            Text(
                                text = if (index == currentTimeTemperatureIndex.value) hourlyForecast.weatherDescription else SimpleDateFormat("h a", Locale.getDefault()).format(hourlyForecast.time),
                                style = if (index == currentTimeTemperatureIndex.value) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                fontWeight = if (index == currentTimeTemperatureIndex.value) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center,
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 24.dp)
                                    .drawBehind {
                                        // draw a temperature line
                                        val temperature = hourlyForecast.temperature.getValue(unit)
                                        val difference = maxTemperature - minTemperature
                                        val percentage = (temperature - minTemperature) / difference
                                        val y = size.height * (1 - percentage)
                                        drawLine(
                                            color = textColor.copy(alpha = 0.8f),
                                            start = Offset(0f, y),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = 16.dp.toPx(),
                                            cap = StrokeCap.Round,
                                        )
                                    },
                            )
                            Text(
                                text = hourlyForecast.temperature.display(unit),
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                    }
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(0.4f),
                    )
                }
            }
            FadeAnimation(animationKey = currentTimeTemperatureIndex.value) {
                state.hourlyForecastInfo?.hourInfo?.getOrNull(currentTimeTemperatureIndex.value)?.let { hourlyForecast ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        state = rememberLazyListState(),
                    ){
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = SimpleDateFormat("h a", Locale.getDefault()).format(hourlyForecast.time),
                                style = MaterialTheme.typography.headlineSmall,
                                color = textColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                            Text(
                                text = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(hourlyForecast.time),
                                style = MaterialTheme.typography.titleSmall,
                                color = textColor,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        stickyHeader {
                            HorizontalDivider()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            HourlyForecastDetails(
                                title = "Feels Like",
                                value = hourlyForecast.feelsLike.getValue(unit).toString(),
                                unit = "°",
                                icon = R.drawable.ic_feels_like_temp,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HourlyForecastDetails(
                                title = "Wind",
                                value = hourlyForecast.windSpeed.getValue(unit).toString(),
                                unit = "km/h",
                                icon = R.drawable.ic_wind,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HourlyForecastDetails(
                                title = "Humidity",
                                value = hourlyForecast.humidity.toString(),
                                unit = "%",
                                icon = R.drawable.ic_humidity,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HourlyForecastDetails(
                                title = "Wind Direction",
                                value = hourlyForecast.windDirection.toString(),
                                unit = "°",
                                icon = R.drawable.ic_pressure,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HourlyForecastDetails(
                                title = "Gust",
                                value = hourlyForecast.windGust.getValue(unit).toString(),
                                unit = "km/h",
                                icon = R.drawable.ic_direction,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HourlyForecastDetails(
                                title = "Precipitation",
                                value = hourlyForecast.precipitation.getValue(unit).toString(),
                                unit = "mm",
                                icon = R.drawable.ic_precipitation,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HourlyForecastDetails(
                                title = "Visibility",
                                value = hourlyForecast.visibility.toString(),
                                unit = "km",
                                icon = R.drawable.ic_visibility,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            //humid
                            HourlyForecastDetails(
                                title = "UV Index",
                                value = hourlyForecast.uvIndex.toString(),
                                unit = "of 10",
                                icon = R.drawable.ic_uv,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HourlyForecastDetails(
    title: String,
    value: String,
    unit: String,
    @DrawableRes icon: Int
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(8.dp)
                .size(32.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = unit,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.offset {
                IntOffset(0, (if(unit.length == 1) -5 else 2).dp.roundToPx())
            }
        )
    }
}
