package com.app.skycast.presentation.screen.current_weather

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.R
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.presentation.ui.components.AppBar
import com.app.skycast.presentation.ui.components.LineChart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentWeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrentWeatherViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val unit = remember { UnitType.METRIC }
    val textColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                title = "Today's Weather",
                onBack = onBack
            )
        }
    ){ innerPadding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ){
            if(state.currentWeather != null && state.forecast != null){
                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .height(115.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = state.currentWeather!!.temp.getValue(unit).toString(),
                                fontSize = 110.dp.value.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                when(unit){
                                    UnitType.METRIC -> "°C"
                                    UnitType.IMPERIAL -> "°F"
                                },
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(top = 8.dp),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = state.currentWeather!!.condition.getDrawableIcon(state.currentWeather!!.isDay)),
                                contentDescription = state.currentWeather!!.condition.toString(),
                                modifier = Modifier
                                    .fillMaxHeight(0.8f),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            "Feels like ${state.currentWeather!!.feelLikeTemp?.getValue(unit)} ${when(unit){
                                UnitType.METRIC -> "°C"
                                UnitType.IMPERIAL -> "°F"
                            }}",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            "${state.currentWeather!!.location?.city}, ${state.currentWeather!!.location?.country}",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            state.forecast!!.description.let {
                                it.ifBlank { "No description available" }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                CurrentWeatherInfoItem(
                                    title = "Wind",
                                    value = state.currentWeather!!.wind?.speed?.display(unit) ?: "N/A",
                                    icon = R.drawable.ic_wind
                                )
                                CurrentWeatherInfoItem(
                                    title = "Precipitation",
                                    value = state.currentWeather!!.precipitation?.display(unit) ?: "N/A",
                                    icon = R.drawable.ic_precipitation,
                                )
                                CurrentWeatherInfoItem(
                                    title = "Humidity",
                                    value = "${state.currentWeather!!.humidity} %",
                                    icon = R.drawable.ic_humidity
                                )
                                CurrentWeatherInfoItem(
                                    title = "UV Index",
                                    value = "${state.currentWeather!!.uv} of 11",
                                    icon = R.drawable.ic_uv
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                CurrentWeatherInfoItem(
                                    title = "Pressure",
                                    value = state.currentWeather!!.pressure?.display(unit) ?: "N/A",
                                    icon = R.drawable.ic_pressure
                                )
                                CurrentWeatherInfoItem(
                                    title = "Cloud",
                                    value = "${state.currentWeather!!.cloud} %",
                                    icon = R.drawable.ic_cloud,
                                )
                                CurrentWeatherInfoItem(
                                    title = "Visibility",
                                    value = "${state.forecast!!.days.firstOrNull()?.visibility} km",
                                    icon = R.drawable.ic_visibility
                                )
                                CurrentWeatherInfoItem(
                                    title = "Dew",
                                    value = "${state.forecast!!.days.firstOrNull()?.dew}",
                                    icon = R.drawable.ic_dew
                                )
                            }
                        }
                    }
                    item {
                        LineChart(
                            dataPoints = state.forecast!!.days.firstOrNull()?.hourInfo?.map { it.temperature.getValue(unit) } ?: emptyList(),
                            xLabels = state.forecast!!.days.firstOrNull()?.hourInfo?.map {
                                SimpleDateFormat(
                                    "h a",
                                    Locale.getDefault()
                                ).format(it.time)
                            } ?: emptyList(),
                            yLabelFormatter =  { it.toString() },
                            chartTitle = "Temperature (°${if(unit == UnitType.METRIC) "C" else "F"})",
                            textColor = textColor,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            "Last updated ${state.lastUpdatedTimestamp?.let {
                                SimpleDateFormat(
                                    "hh:mm a",
                                    Locale.getDefault()
                                ).format(it)
                            }}",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Normal
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun CurrentWeatherInfoItem(
    title: String,
    value: String,
    @DrawableRes icon: Int,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        ){
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}