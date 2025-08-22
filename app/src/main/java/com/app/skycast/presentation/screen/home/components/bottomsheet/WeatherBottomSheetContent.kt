package com.app.skycast.presentation.screen.home.components.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.skycast.R
import com.app.skycast.core.animation.FadeAnimation
import com.app.skycast.domain.model.current_weather.CurrentWeather
import com.app.skycast.domain.model.forecast_weather.ForecastDayInfo
import com.app.skycast.domain.model.forecast_weather.ForecastWeather
import com.app.skycast.domain.model.units.AirQuality
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.presentation.screen.home.WeatherInfoStatus
import com.app.skycast.presentation.screen.home.components.forecast.ForecastChip
import com.app.skycast.presentation.screen.home.components.forecast.ForecastItem
import com.app.skycast.presentation.screen.home.components.list.BoxContainer
import com.app.skycast.presentation.screen.home.components.list.Title
import com.app.skycast.presentation.screen.home.components.weather.AirQualitySubItem
import com.app.skycast.presentation.screen.home.components.weather.CurrentWeatherInfo
import com.app.skycast.presentation.screen.home.components.weather.TemperatureData
import com.app.skycast.presentation.screen.home.components.weather.TemperatureGraph
import com.app.skycast.presentation.ui.components.ShimmerEffect
import com.app.skycast.presentation.util.drawMarker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherBottomSheetContent(
    weatherInfo: WeatherInfoStatus,
    sheetHeight: Dp = 90.dp,
    navigateToCurrentWeather: () -> Unit,
    navigateToHourlyForecast: () -> Unit,
    navigateToForecast: () -> Unit,
    navigateToAqi: () -> Unit,
) {
    val scrollState = rememberLazyListState()
    when (weatherInfo) {
        is WeatherInfoStatus.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(30.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(30.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                )
            }
        }
        is WeatherInfoStatus.Success -> {
            val selectedIndex = remember { mutableStateOf(0) }
            val currentForecastDay =
                weatherInfo.altOtherDaysForecast?.days?.getOrNull(selectedIndex.value)

            LazyColumn(
                modifier = Modifier
                    .requiredHeight(sheetHeight)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                state = scrollState,
            ) {
                CurrentWeatherSection(
                    weatherInfo.currentWeather,
                    navigateToCurrentWeather = navigateToCurrentWeather,
                )
                HourlyForecastSection(
                    weatherInfo.currentDayForecast,
                    onNavigateToHourlyForecast = navigateToHourlyForecast
                )
                DailyForecastSection(
                    weatherInfo,
                    currentForecastDay,
                    selectedIndex.value,
                    navigateToForecast,
                ) { selectedIndex.value = it }
                AirQualitySection(weatherInfo.airQuality, navigateToAqi = navigateToAqi)
                AstroSection(weatherInfo)
                BottomSpace()
            }
        }
    }
}


private fun LazyListScope.CurrentWeatherSection(
    currentWeather: CurrentWeather,
    navigateToCurrentWeather: () -> Unit
) {
    Title(
        title = "Current Weather",
        modifier = Modifier.padding(
            top = 16.dp,
            start = 16.dp,
            bottom = 8.dp
        ),
        onClick = navigateToCurrentWeather
    )
    BoxContainer {
        CurrentWeatherInfo(
            icon = R.drawable.ic_feels_like_temp,
            title = "Feels like temp",
            value = currentWeather.feelLikeTemp?.display(UnitType.METRIC) ?: "--",
        )
        HorizontalDivider()
        CurrentWeatherInfo(
            icon = R.drawable.ic_wind,
            title = "Wind",
            value = currentWeather.wind?.speed?.display(UnitType.METRIC) ?: "--",
        )
        HorizontalDivider()
        CurrentWeatherInfo(
            icon = R.drawable.ic_humidity,
            title = "Humidity",
            value = "${currentWeather.humidity}%",
        )
        HorizontalDivider()
        CurrentWeatherInfo(
            icon = R.drawable.ic_uv,
            title = "UV Index",
            value = "${currentWeather.uv}",
        )
        HorizontalDivider()
        CurrentWeatherInfo(
            icon = R.drawable.ic_precipitation,
            title = "Precipitation",
            value = currentWeather.precipitation?.display(UnitType.METRIC) ?: "--",
        )
        HorizontalDivider()
        CurrentWeatherInfo(
            icon = R.drawable.ic_cloud,
            title = "Cloud",
            value = "${currentWeather.cloud}%",
        )
    }
}

private fun LazyListScope.HourlyForecastSection(
    currentDayForecast: ForecastWeather,
    onNavigateToHourlyForecast: () -> Unit
) {
    Title(title = "Hourly forecast", onClick = onNavigateToHourlyForecast)
    if (currentDayForecast.forecast?.firstOrNull()?.updates != null) {
        BoxContainer {
            TemperatureGraph(
                temperatures = currentDayForecast.forecast.firstOrNull()?.updates!!.map {
                    TemperatureData(
                        temperature = it.temp.getValue(UnitType.METRIC),
                        weatherIcon = it.condition.getDrawableIcon(it.isDay),
                        time = SimpleDateFormat("hh a", Locale.getDefault()).format(it.time)
                    )
                },
                lineColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSurface,
                temperatureUnit = "°C",
                segmentLength = 60.dp,
                graphHeight = 80.dp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

private fun LazyListScope.DailyForecastSection(
    weatherInfo: WeatherInfoStatus.Success,
    currentForecastDay: ForecastDayInfo?,
    selectedIndex: Int,
    navigateToForecast: () -> Unit,
    onSelected: (Int) -> Unit,
) {
    Title(title = "Today's forecast", onClick = navigateToForecast)
    BoxContainer {
        DaySelector(weatherInfo, selectedIndex, onSelected = onSelected)
        HorizontalDivider()
        DailyForecastDetails(currentForecastDay)
    }
    ForecastMetrics(currentForecastDay)
}

@Composable
private fun DaySelector(
    weatherInfo: WeatherInfoStatus.Success,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    Row {
        weatherInfo.altOtherDaysForecast?.days?.forEach { forecastDay ->
            DaySelectorItem(
                forecastDay = forecastDay,
                isSelected = weatherInfo.altOtherDaysForecast.days.indexOf(forecastDay) == selectedIndex,
                onSelected = { onSelected(weatherInfo.altOtherDaysForecast.days.indexOf(forecastDay)) }
            )
            if (forecastDay != weatherInfo.altOtherDaysForecast.days.last()) {
                VerticalDivider(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun RowScope.DaySelectorItem(
    forecastDay: ForecastDayInfo,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onSelected)
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceVariant
                else Color.Transparent
            )
            .height(60.dp)
            .weight(1f),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = SimpleDateFormat("d", Locale.getDefault()).format(forecastDay.localTime),
                style = if (isSelected) MaterialTheme.typography.titleMedium
                else MaterialTheme.typography.titleSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
            Text(
                text = SimpleDateFormat("EEE", Locale.getDefault()).format(forecastDay.localTime),
                style = if (isSelected) MaterialTheme.typography.bodyMedium
                else MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun DailyForecastDetails(currentForecastDay: ForecastDayInfo?) {
    FadeAnimation(animationKey = currentForecastDay.hashCode()) {
        Column {
            HourlyForecastList(currentForecastDay)
            WeatherMetrics(currentForecastDay)
        }
    }
}

@Composable
private fun HourlyForecastList(currentForecastDay: ForecastDayInfo?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        currentForecastDay?.hourInfo?.forEach { hourForecast ->
            ForecastChip(
                date = hourForecast.time,
                temp = hourForecast.temperature.display(UnitType.METRIC),
                icon = hourForecast.weatherCondition.getDrawableIcon(hourForecast.isDay),
            )
        }
    }
}

@Composable
private fun WeatherMetrics(currentForecastDay: ForecastDayInfo?) {
    HorizontalDivider()
    CurrentWeatherInfo(
        icon = R.drawable.ic_feels_like_temp,
        title = "Feels like temp",
        value = currentForecastDay?.feelsLikeTempStat?.avg?.display(UnitType.METRIC) ?: "--",
    )
    HorizontalDivider()
    CurrentWeatherInfo(
        icon = R.drawable.ic_wind,
        title = "Wind",
        value = "${currentForecastDay?.windSpeed?.display(UnitType.METRIC) ?: "--"}, ${currentForecastDay?.windDirection?.toInt() ?: "--"}°",
    )
    HorizontalDivider()
    CurrentWeatherInfo(
        icon = R.drawable.ic_humidity,
        title = "Humidity",
        value = "${currentForecastDay?.humidity}%",
    )
    HorizontalDivider()
    CurrentWeatherInfo(
        icon = R.drawable.ic_visibility,
        title = "Visibility",
        value = "${currentForecastDay?.visibility} km",
    )
}

private fun LazyListScope.AirQualitySection(airQuality: AirQuality?, navigateToAqi: () -> Unit) {
    Title(title = "Air Quality", onClick = navigateToAqi)
    if (airQuality != null) {
        BoxContainer {
            AirQualityContent(airQuality)
        }
    }
}

private fun LazyListScope.ForecastMetrics(currentForecastDay: ForecastDayInfo?) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
    }
    item {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ForecastItem(
                title = "Sunrise",
                icon = R.drawable.ic_sunrise,
                value = SimpleDateFormat(
                    "hh:mm",
                    Locale.getDefault()
                ).format(currentForecastDay?.astroInfo?.sunrise ?: Date()),
                unit = SimpleDateFormat(
                    "a",
                    Locale.getDefault()
                ).format(currentForecastDay?.astroInfo?.sunrise ?: Date()),
            )
            ForecastItem(
                title = "Sunset",
                icon = R.drawable.ic_sunset,
                value = SimpleDateFormat(
                    "hh:mm",
                    Locale.getDefault()
                ).format(currentForecastDay?.astroInfo?.sunset ?: Date()),
                unit = SimpleDateFormat(
                    "a",
                    Locale.getDefault()
                ).format(currentForecastDay?.astroInfo?.sunset ?: Date()),
            )
            ForecastItem(
                title = "UV Index",
                icon = R.drawable.ic_uv,
                value = currentForecastDay?.uvIndex.toString(),
                unit = "of 11",
            )
        }
    }
    item {
        Spacer(modifier = Modifier.height(16.dp))
    }
    item {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ForecastItem(
                title = "Pressure",
                icon = R.drawable.ic_pressure,
                value = currentForecastDay?.pressure?.getValue(UnitType.METRIC).toString(),
                unit = "hPa",
            )
            ForecastItem(
                title = "Wind Gust",
                icon = R.drawable.ic_wind,
                value = currentForecastDay?.windGust?.getValue(UnitType.METRIC).toString(),
                unit = "km/h",
            )
            ForecastItem(
                title = "Wind Speed",
                icon = R.drawable.ic_wind,
                value = currentForecastDay?.windSpeed?.getValue(UnitType.METRIC).toString(),
                unit = "km/h",
            )
        }
    }
}

@Composable
private fun AirQualityContent(airQuality: AirQuality) {
    val requiredHeight = MaterialTheme.typography.headlineSmall.fontSize.value.dp + 28.dp
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AirQualityValue(airQuality)
        VerticalDivider(
            modifier = Modifier.height(requiredHeight)
        )
        AirQualityMetrics(airQuality, requiredHeight)
    }
    HorizontalDivider()
    AirQualityDescription(airQuality)
    AirQualityIndicator(airQuality)
}

@Composable
private fun RowScope.AirQualityValue(airQuality: AirQuality) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = airQuality.value.toInt().toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun RowScope.AirQualityMetrics(airQuality: AirQuality, requiredHeight: Dp) {
    Row(
        modifier = Modifier.weight(3f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AirQualitySubItem(title = "O₃", value = airQuality.o3.toInt().toString())
        VerticalDivider(
            modifier = Modifier.height(requiredHeight)
        )
        AirQualitySubItem(title = "NO₂", value = airQuality.no2.toInt().toString())
        VerticalDivider(
            modifier = Modifier.height(requiredHeight)
        )
        AirQualitySubItem(title = "SO₂", value = airQuality.so2.toInt().toString())
        VerticalDivider(
            modifier = Modifier.height(requiredHeight)
        )
        AirQualitySubItem(title = "CO", value = airQuality.co.toInt().toString())
    }
}

@Composable
private fun AirQualityDescription(airQuality: AirQuality) {
    Text(
        text = airQuality.toDescription(),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(8.dp),
    )
}

@Composable
private fun AirQualityIndicator(airQuality: AirQuality) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 12.dp, top = 4.dp)
            .height(12.dp)
            .background(
                Brush.horizontalGradient(
                    colors = AirQuality.colorGradientList,
                ),
                shape = MaterialTheme.shapes.small
            )
            .drawMarker(
                percent = airQuality.value.toFloat() / 400,
                color = MaterialTheme.colorScheme.onSurface
            )
    )
}

private fun LazyListScope.AstroSection(weatherInfo: WeatherInfoStatus.Success) {
    Title(title = "Astro", showButton = false)
    BoxContainer(
        modifier = Modifier.padding(8.dp)
    ) {
        AstroContent(weatherInfo)
    }
}

@Composable
private fun AstroContent(weatherInfo: WeatherInfoStatus.Success) {
    val isDay = weatherInfo.currentDayForecast.currentInfo?.isDay ?: true
    val startTime = getStartTime(weatherInfo, isDay)
    val endTime = getEndTime(weatherInfo, isDay)
    val currentTime = weatherInfo.currentDayForecast.currentInfo?.localTime ?: Date()
    val percent = calculateTimePercent(startTime, endTime, currentTime)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = if (isDay) R.drawable.ic_sunrise else R.drawable.ic_sunset),
            contentDescription = if (isDay) "Sunrise" else "Sunset",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        )
        Image(
            painter = painterResource(id = if (isDay) R.drawable.ic_sunset else R.drawable.ic_sunrise),
            contentDescription = if (isDay) "Sunset" else "Sunrise",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            if (isDay) "Sunrise" else "Sunset",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            if (isDay) "Sunset" else "Sunrise",
            style = MaterialTheme.typography.bodySmall,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .drawMarker(
                    percent = percent.coerceIn(0f, 1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(8.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    MaterialTheme.shapes.small
                )
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(startTime),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(endTime),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun getStartTime(weatherInfo: WeatherInfoStatus.Success, isDay: Boolean): Date {
    return if (isDay) {
        weatherInfo.currentDayForecast.forecast?.firstOrNull()?.astro?.sunrise
    } else {
        weatherInfo.currentDayForecast.forecast?.firstOrNull()?.astro?.sunset
    } ?: Date()
}

private fun getEndTime(weatherInfo: WeatherInfoStatus.Success, isDay: Boolean): Date {
    return if (isDay) {
        weatherInfo.currentDayForecast.forecast?.firstOrNull()?.astro?.sunset
    } else {
        weatherInfo.otherDaysForecast?.forecast?.firstOrNull()?.astro?.sunrise
    } ?: Date()
}

private fun calculateTimePercent(startTime: Date, endTime: Date, currentTime: Date): Float {
    val start = startTime.time
    val end = endTime.time
    val current = currentTime.time
    val total = end - start
    val currentPercent = current - start
    return (currentPercent.toFloat() / total).coerceIn(0f, 1f)
}

private fun LazyListScope.BottomSpace() {
    item {
        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(60.dp),
        )
    }
}

