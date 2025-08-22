package com.app.skycast.presentation.screen.forecast

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.R
import com.app.skycast.domain.model.forecast_weather.ForecastHourInfo
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.presentation.ui.components.AppBar
import com.app.skycast.presentation.ui.components.ShimmerEffect
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ForecastScreen(
    modifier: Modifier = Modifier,
    viewModel: ForecastViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val dayScrollState = rememberScrollState()
    val hourScrollState = rememberLazyListState()
    Scaffold(
        topBar = {
            AppBar(
                title = "Today's Forecast",
                onBack = onBack
            )
        },
        modifier = Modifier.fillMaxSize(),
    ){ innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ){
            if(state.forecastWeather != null){
                val selectedIndex = remember { mutableIntStateOf(0) }
                Row(
                    modifier = Modifier
                        .horizontalScroll(dayScrollState)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                ) {
                    state.forecastWeather?.days?.forEachIndexed { index, day ->
                        Column(
                            modifier = Modifier
                                .height(80.dp)
                                .aspectRatio(3 / 4f)
                                .clickable {
                                    selectedIndex.intValue = index
                                }
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (selectedIndex.intValue == index) 1f else 0.3f)
                                )
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = SimpleDateFormat("dd", Locale.getDefault()).format(day.localTime),
                                style = if (selectedIndex.intValue == index) MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ) else MaterialTheme.typography.titleLarge,
                                fontWeight = if (selectedIndex.intValue == index) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                text = SimpleDateFormat("EEE", Locale.getDefault()).format(day.localTime),
                                fontWeight = if (selectedIndex.intValue == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
                HorizontalDivider()
                LazyColumn(
                    state = hourScrollState,
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    state.forecastWeather?.days?.get(selectedIndex.intValue)?.hourInfo?.forEach { hour ->
                        item {
                            HourlyForecastItem(
                                hour = hour,
                                isFirst = hour == state.forecastWeather?.days?.get(selectedIndex.intValue)?.hourInfo?.first(),
                            )
                        }
                    } ?: item {
                        ShimmerForecast()
                    }
                }
            } else {
                ShimmerForecast()
            }
        }
    }
}

@Composable
fun ShimmerForecast() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(5){
                ShimmerEffect(
                    modifier = Modifier
                        .height(80.dp)
                        .aspectRatio(3 / 4f)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(25.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(25.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        repeat(4){
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HourlyForecastItem(
    hour: ForecastHourInfo,
    isFirst: Boolean,
    height: Float = 55f,
    expandedHeight: Float = 250f
) {
    val isExpanded = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .animateContentSize()
            .height(if (isExpanded.value) expandedHeight.dp else height.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (!isFirst) {
                VerticalDivider(
                    modifier = Modifier.height((height * 0.3).dp)
                )
            } else {
                Spacer(modifier = Modifier.height((height * 0.3).dp))
            }
            Box(
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                    .size(20.dp)
                    .clip(CircleShape)
                    .padding(5.dp)
                    .background(MaterialTheme.colorScheme.onSurface, CircleShape)
            )
            VerticalDivider(
                modifier = Modifier
                    .height(if (isExpanded.value) expandedHeight.dp else height.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(hour.time),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = hour.weatherCondition.toString(),
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = hour.weatherCondition.getDrawableIcon(hour.isDay)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = hour.temperature.display(UnitType.METRIC),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .rotate(if (isExpanded.value) 0f else 180f)
                        .clickable {
                            isExpanded.value = !isExpanded.value
                        }
                )
            }
            if (isExpanded.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(expandedHeight.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                    ){
                        ForecastItem(
                            icon = R.drawable.ic_humidity,
                            title = "Humidity",
                            value = "${hour.humidity}%",
                        )
                        HorizontalDivider()
                        ForecastItem(
                            icon = R.drawable.ic_wind,
                            title = "Wind",
                            value = hour.windSpeed.display(UnitType.METRIC),
                        )
                        HorizontalDivider()
                        ForecastItem(
                            icon = R.drawable.ic_feels_like_temp,
                            title = "Feels like",
                            value = hour.feelsLike.display(UnitType.METRIC),
                        )
                        HorizontalDivider()
                        ForecastItem(
                            icon = R.drawable.ic_pressure,
                            title = "Wind Dir",
                            value = "${hour.windDirection}°",
                        )
                        HorizontalDivider()
                        ForecastItem(
                            icon = R.drawable.ic_visibility,
                            title = "Visibility",
                            value = "${hour.visibility} km",
                        )
                        HorizontalDivider()
                        ForecastItem(
                            icon = R.drawable.ic_uv,
                            title = "UV Index",
                            value = hour.uvIndex.toString(),
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun ForecastItem(
    @DrawableRes icon: Int,
    title: String,
    value: String,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}