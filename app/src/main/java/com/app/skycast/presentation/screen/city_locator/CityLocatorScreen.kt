package com.app.skycast.presentation.screen.city_locator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityLocatorScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: CityLocatorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0]
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val cityListState = rememberLazyListState()

    LaunchedEffect(state.searchQuery) {
        delay(500)
        if (state.searchQuery.isNotEmpty()) {
            viewModel.trySendAction(CityLocatorAction.SearchCity(state.searchQuery))
        }
    }
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = cityListState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == cityListState.layoutInfo.totalItemsCount - 10
        }
    }
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            viewModel.trySendAction(CityLocatorAction.LoadNextPage)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        value = state.searchQuery,
                        onValueChange = {
                            viewModel.trySendAction(CityLocatorAction.SearchQuery(it))
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        placeholder = { Text("Search for a city...") },
                        trailingIcon = {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 1.5.dp
                                )
                            } else if (state.searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        viewModel.trySendAction(CityLocatorAction.SearchQuery(""))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            HorizontalDivider()
            if (state.searchQuery.isEmpty()) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState),
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Popular Cities",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                    state.popularCities.forEach { popularCity ->
                        Column(
                            modifier = Modifier
                                .clickable{
                                    viewModel.trySendAction(CityLocatorAction.SelectPopularCity(popularCity))
                                }.padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = popularCity.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Text(
                                    text = popularCity.country,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Lat: " + String.format(
                                        locale,
                                        "%.2f",
                                        popularCity.latitude
                                    ) + ", Lon: " + String.format(
                                        locale,
                                        "%.2f",
                                        popularCity.longitude
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.height(14.dp)
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            } else {
                LazyColumn(
                    state = cityListState,
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Search Results",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                    items(state.searchedCities.size) { index ->
                        val searchedCity = state.searchedCities[index]
                        Column(
                            modifier = Modifier
                                .clickable {
                                    viewModel.trySendAction(
                                        CityLocatorAction.SelectSearchCity(searchedCity)
                                    )
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Text(
                                buildAnnotatedString {
                                    val query = state.searchQuery
                                    val name = searchedCity.name
                                    val startIndex = name.indexOf(query, ignoreCase = true)
                                    val endIndex = startIndex + query.length
                                    if (startIndex == -1 || endIndex == -1) {
                                        append(name)
                                        return@buildAnnotatedString
                                    }
                                    withStyle(MaterialTheme.typography.bodyLarge.toSpanStyle()) {
                                        append(name.substring(0, startIndex))
                                    }
                                    withStyle(MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold).toSpanStyle()) {
                                        append(name.substring(startIndex, endIndex))
                                    }
                                    withStyle(MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)).toSpanStyle()) {
                                        append(name.substring(endIndex, name.length))
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Text(
                                    text = searchedCity.country,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Lat: " + String.format(
                                        locale,
                                        "%.2f",
                                        searchedCity.latitude
                                    ) + ", Lon: " + String.format(
                                        locale,
                                        "%.2f",
                                        searchedCity.longitude
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.height(14.dp)
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}