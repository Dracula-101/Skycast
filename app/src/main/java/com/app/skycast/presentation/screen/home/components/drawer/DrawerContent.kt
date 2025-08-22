package com.app.skycast.presentation.screen.home.components.drawer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.skycast.BuildConfig
import com.app.skycast.R
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.presentation.screen.home.HomeState
import com.app.skycast.presentation.screen.home.WeatherInfoStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WeatherDrawerContent(
    state: HomeState,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navigateToAddLocations: () -> Unit,
    navigateToForecast: () -> Unit,
    navigateToNews: () -> Unit,
    navigateToAqiInfo: () -> Unit
) {
    val context = LocalContext.current
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.7f),
    ) {
        WeatherHeader(state.weatherInfoStatus)
        DrawerNavigation(
            drawerState,
            scope,
            DrawerNavigation(
                navigateToForecast = navigateToForecast,
                navigateToLocations = navigateToAddLocations,
                navigateToNews = navigateToNews,
                navigateToAqiInfo = navigateToAqiInfo,
                navigateToPrivacyPolicy = {
                    // open url
                    val url = "https://dracula-101.github.io/skycast-privacy-policy/"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                navigateToSettings = {}
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        AppVersionInfo()
    }
}

@Composable
private fun WeatherHeader(weatherInfoStatus: WeatherInfoStatus) {
    when(weatherInfoStatus) {
        is WeatherInfoStatus.Loading -> {}
        is WeatherInfoStatus.Success -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 12.dp)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        weatherInfoStatus.currentWeather.temp.display(UnitType.METRIC),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        "${weatherInfoStatus.currentWeather.location?.city}, ${weatherInfoStatus.currentWeather.location?.country}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Image(
                    painter = painterResource(id = weatherInfoStatus.currentWeather.condition.getDrawableIcon(weatherInfoStatus.currentWeather.isDay)),
                    contentDescription = "Weather Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(62.dp)
                )
            }
        }
    }
    HorizontalDivider(modifier = Modifier.fillMaxWidth())
}


@Composable
private fun DrawerNavigation(
    drawerState: DrawerState,
    scope: CoroutineScope,
    drawerNavigation: DrawerNavigation
) {
    val drawerItems = listOf(
        DrawerItemData(R.drawable.ic_forecast, DrawerSection.Forecast),
        DrawerItemData(R.drawable.ic_location_pin, DrawerSection.Locations),
        DrawerItemData(R.drawable.ic_news, DrawerSection.News),
        DrawerItemData(R.drawable.ic_wind, DrawerSection.AqiInfo),
    )

    drawerItems.forEach { drawerItem ->
        DrawerItem(
            modifier = Modifier.fillMaxWidth(),
            icon = drawerItem.icon,
            title = drawerItem.section.toString(),
            onClick = {
                when(drawerItem.section) {
                    DrawerSection.Forecast -> drawerNavigation.navigateToForecast()
                    DrawerSection.Locations -> drawerNavigation.navigateToLocations()
                    DrawerSection.News -> drawerNavigation.navigateToNews()
                    DrawerSection.AqiInfo -> drawerNavigation.navigateToAqiInfo()
                }
                scope.launch { drawerState.close() }
            }
        )
    }
    HorizontalDivider(modifier = Modifier.fillMaxWidth())
    DrawerItem(
        modifier = Modifier.fillMaxWidth(),
        icon = R.drawable.ic_info,
        title = "Privacy Policy",
        onClick = {
            drawerNavigation.navigateToPrivacyPolicy()
            scope.launch { drawerState.close() }
        }
    )
}

@Composable
private fun AppVersionInfo() {
    val context = LocalContext.current
    Row(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "${context.getString(R.string.app_name)}, Version ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}