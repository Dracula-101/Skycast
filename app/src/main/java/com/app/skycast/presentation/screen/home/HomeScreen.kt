package com.app.skycast.presentation.screen.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.R
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.units.UnitType
import com.app.skycast.presentation.screen.home.components.CenterWeatherInfo
import com.app.skycast.presentation.screen.home.components.bottomsheet.WeatherBottomSheetContent
import com.app.skycast.presentation.screen.home.components.drawer.WeatherDrawerContent
import com.app.skycast.presentation.ui.util.WeatherBg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToAddLocations: () -> Unit,
    navigateToForecast: () -> Unit,
    navigateToNews: () -> Unit,
    navigateToAqiInfo: () -> Unit,
    navigateToCurrentWeather: () -> Unit,
    navigateToHourlyForecast: () -> Unit,
    navigateToAqi: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // Screen state
    val isExpanded = remember { mutableStateOf(false) }
    val topBarAnimation = remember { Animatable(0f) }

    // UI Configuration
    val density = LocalDensity.current
    val textColor = MaterialTheme.colorScheme.onSurface
    val topBarOffset = 90.dp

    // Navigation states
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scaffoldState = createBottomSheetState(isExpanded)

    // Effects
    LaunchedEffect(state.weatherInfoStatus.hashCode()){
        launch {
            scaffoldState.bottomSheetState.partialExpand()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isExpanded.value }
            .distinctUntilChanged()
            .collect { expanded ->
                topBarAnimation.animateTo(if (expanded) 1f else 0f)
            }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isExpanded.value,
        drawerContent = {
            WeatherDrawerContent(
                state = state,
                drawerState = drawerState,
                scope = scope,
                navigateToAddLocations = navigateToAddLocations,
                navigateToForecast = navigateToForecast,
                navigateToNews = navigateToNews,
                navigateToAqiInfo = navigateToAqiInfo,
            )
        }
    ) {
        BoxWithConstraints {
            val sheetHeight = with(density) { constraints.maxHeight.toDp() - topBarOffset }
            BottomSheetScaffold(
                sheetContent = {
                    WeatherBottomSheetContent(
                        weatherInfo = state.weatherInfoStatus,
                        sheetHeight = sheetHeight,
                        navigateToCurrentWeather = navigateToCurrentWeather,
                        navigateToHourlyForecast = navigateToHourlyForecast,
                        navigateToForecast = navigateToForecast,
                        navigateToAqi = navigateToAqi
                    )
                },
                scaffoldState = scaffoldState,
                sheetSwipeEnabled = false,
                sheetPeekHeight = 380.dp,
                sheetShape = RoundedCornerShape(
                    topStart = (24 * (if(isExpanded.value) 0 else 1)).dp,
                    topEnd = (24 * (if(isExpanded.value) 0 else 1)).dp
                ),
                sheetDragHandle = {
                    if(!isExpanded.value){
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .fillMaxWidth()
                                .height(24.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.2f)
                                    .height(4.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues),
                ) {
                    WeatherBackground(
                        localTime = state.localTime
                    )
                    when (val weatherInfo = state.weatherInfoStatus) {
                        is WeatherInfoStatus.Success -> {
                            val weatherBg = WeatherBg.fromDate(weatherInfo.currentWeather.localTime)
                            CenterWeatherInfo(
                                animationKey = weatherInfo.currentWeather.hashCode(),
                                textColor = weatherBg.getBodyTextColor(),
                                blurColor = weatherBg.getDominantColor(),
                                weatherCondition = weatherInfo.currentWeather.condition.toString(),
                                date = weatherInfo.currentWeather.localTime,
                                location = weatherInfo.currentWeather.location?.city.toString(),
                                temperature = weatherInfo.currentWeather.temp.getValue(UnitType.METRIC),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(450.dp),
                                isLoading = state.isLoading,
                                onClick = {
                                    viewModel.trySendAction(HomeAction.FetchCurrentWeatherInfo)
                                }
                            )
                            WeatherTopBar(
                                weatherInfo = weatherInfo,
                                favouriteUserLocations = state.favouriteUserLocations,
                                onChangeLocation = { userLocation ->
                                    viewModel.trySendAction(HomeAction.ChangeUserLocation(userLocation))
                                },
                                topBarOffset = topBarOffset,
                                topBarAnimation = topBarAnimation,
                                textColor = textColor,
                                drawerState = drawerState,
                                scope = scope
                            )
                        }
                        WeatherInfoStatus.Loading -> {}
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun createBottomSheetState(isExpanded: MutableState<Boolean>) =
    rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState(
            confirmValueChange = { value ->
                isExpanded.value = value == SheetValue.Expanded
                value != SheetValue.Hidden
            }
        )
    )


@Composable
private fun WeatherBackground(localTime: Date?) {
    if (localTime == null) return
    val weatherBg = WeatherBg.fromDate(localTime)
    Image(
        painter = painterResource(id = weatherBg.toBackground()),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
    )
}

@Composable
private fun BoxScope.WeatherTopBar(
    weatherInfo: WeatherInfoStatus.Success,
    favouriteUserLocations: List<UserLocation>?,
    onChangeLocation: (UserLocation) -> Unit,
    topBarOffset: Dp,
    topBarAnimation: Animatable<Float, AnimationVector1D>,
    textColor: Color,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    val isPopupOpen = rememberSaveable { mutableStateOf(false) }
    val rotationAnimation = remember { Animatable(0f) }
    LaunchedEffect(isPopupOpen.value) {
        rotationAnimation.animateTo(if (isPopupOpen.value) 180f else 0f)
    }
    Box(
        modifier = Modifier
            .offset {
                val offset = topBarOffset * (1 - topBarAnimation.value)
                IntOffset(
                    0,
                    -offset
                        .toPx()
                        .toInt()
                )
            }
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .align(Alignment.TopCenter)
            .requiredHeight(topBarOffset)
            .statusBarsPadding(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = {
                    scope.launch { drawerState.open() }
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu",
                )
            }
            Column {
                Text(
                    text = "${weatherInfo.currentWeather.location?.city}, ${
                        weatherInfo.currentWeather.temp.display(
                            UnitType.METRIC
                        )
                    }",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                )
                Text(
                    text = "Last Updated on ${
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                        ).format(Date(weatherInfo.lastUpdated))
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.8f),
                )
            }
            Box {
                IconButton(
                    onClick = {
                        isPopupOpen.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Arrow Down",
                        modifier = Modifier.rotate(rotationAnimation.value)
                    )
                }
                if (isPopupOpen.value) {
                    DropdownMenu(
                        expanded = isPopupOpen.value,
                        onDismissRequest = {
                            isPopupOpen.value = false
                        },
                    ) {
                        if (favouriteUserLocations.isNullOrEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "No Locations Added",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Text(
                                    text = "Favourite a location to switch",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textColor.copy(alpha = 0.8f),
                                )
                            }
                        } else {
                            Text(
                                text = "Favourite Locations",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            HorizontalDivider()
                            favouriteUserLocations.forEach { location ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            isPopupOpen.value = false
                                            onChangeLocation(location)
                                        }
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_location_pin),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(textColor),
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                    Column(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = location.city,
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                        Text(
                                            text = location.country ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = textColor.copy(alpha = 0.8f),
                                        )
                                    }
                                }
                                if (favouriteUserLocations.last() != location) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



