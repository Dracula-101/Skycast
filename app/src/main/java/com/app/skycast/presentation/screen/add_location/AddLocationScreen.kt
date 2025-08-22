package com.app.skycast.presentation.screen.add_location

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.R
import com.app.skycast.domain.util.precision
import kotlinx.coroutines.delay

@Composable
fun AddLocationScreen(
    viewModel: AddLocationViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by  viewModel.stateFlow.collectAsStateWithLifecycle()
    val scrollState = rememberLazyListState()
    val keyboardManager = LocalSoftwareKeyboardController.current

    state.dialogState?.let {
        when(it){
            is AddLocationState.DialogState.ShowSaveLocation -> {
                LocationScreenDialog(
                    title = "Save Location",
                    subtitle = "Do you want to save ${it.location.city}, ${it.location.country}?",
                    onDismiss = {
                        viewModel.trySendAction(AddLocationAction.HideSaveLocationDialog)
                    },
                    onSave = {
                        viewModel.trySendAction(AddLocationAction.SaveLocation(it.location))
                    }
                )
            }

            is AddLocationState.DialogState.ChangeCurrentLocation -> {
                LocationScreenDialog(
                    title = "Change Current Location",
                    subtitle = "Do you want to change your current location to ${it.newLocation.city}, ${it.newLocation.country}?",
                    onDismiss = {
                        viewModel.trySendAction(AddLocationAction.HideSaveLocationDialog)
                    },
                    onSave = {
                        viewModel.trySendAction(AddLocationAction.ChangeCurrentLocation(it.newLocation))
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            AddLocationTopBar(
                state = state,
                onBack = {
                    keyboardManager?.hide()
                    onBack()
                },
                onSearch = { query ->
                    viewModel.trySendAction(AddLocationAction.SearchLocation(query))
                }
            )
        }
    ) { padding->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .imePadding(),
            state = scrollState,
        ){
            item {
                Text(
                    text = if(state.searchCities.isNotEmpty()) "Searched Locations" else "Added Locations",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
            }
            if(state.searchCities.isEmpty()){
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .padding(8.dp)
                    ){
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.ic_location_pin),
                                contentDescription = "Location Pin",
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.CenterVertically),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                            Text(
                                text = "Current Location",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        state.currentLocation?.let {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.Bottom,
                            ) {
                                Column {
                                    Text(
                                        text = it.city,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                    Text(
                                        buildString {
                                            append(it.state)
                                            if (it.state?.isNotEmpty() == true && it.country?.isNotEmpty() == true) {
                                                append(", ${it.country}")
                                            }
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Column(
                                    horizontalAlignment = Alignment.End
                                ){
                                    Text(
                                        text = "Lat: ${it.latitude.precision(2)}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "Long: ${it.longitude.precision(2)}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
                items(state.userSavedLocation?.size ?: 0) { index ->
                    val location = state.userSavedLocation?.get(index) ?: return@items
                    LocationItem(
                        city = location.city,
                        country = location.country ?: "",
                        latitude = location.latitude,
                        longitude = location.longitude,
                        isFavourite = location.isFavourite,
                        onClick = {
                            viewModel.trySendAction(
                                AddLocationAction.ChangeCurrentLocationDialog(location)
                            )
                        },
                        onFavouriteClick = {
                            viewModel.trySendAction(
                                AddLocationAction.ChangeFavouriteLocation(location)
                            )
                        }
                    )
                }
            } else {
                items(state.searchCities.size) { index ->
                    val location = state.searchCities[index]
                    LocationItem(
                        city = location.city,
                        country = location.country ?: "",
                        latitude = location.latitude,
                        longitude = location.longitude,
                        showFavourite = false,
                        onClick = {
                            viewModel.trySendAction(
                                AddLocationAction.ShowSaveLocationDialog(location)
                            )
                        }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationItem(
    city: String,
    country: String,
    latitude: Double,
    longitude: Double,
    showIcon: Boolean = true,
    showFavourite: Boolean = true,
    isFavourite: Boolean = false,
    onFavouriteClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 16.dp, end = 8.dp)
    ) {
        if(showIcon){
            Image(
                painter = painterResource(id = R.drawable.ic_location_pin),
                contentDescription = "Location Pin",
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(3f)
        ) {
            Text(
                text = city,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = country,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Lat: ${latitude.precision(2)}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Long: ${longitude.precision(2)}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        if(showFavourite){
            IconButton(
                onClick = onFavouriteClick
            ) {
                Image(
                    painterResource(if(isFavourite) R.drawable.ic_star_filled else R.drawable.ic_star_outline),
                    contentDescription = "Star",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationTopBar(
    state: AddLocationState,
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
) {
    val locationValue = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(locationValue.value) {
        if (locationValue.value == "") return@LaunchedEffect
        delay(300)
        onSearch(locationValue.value)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TopAppBarDefaults.topAppBarColors().scrolledContainerColor)
            .statusBarsPadding()
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
           IconButton(
               onClick = onBack
           ){
                Icon(
                     imageVector = Icons.Rounded.Close,
                     contentDescription = "Back",
                     tint = MaterialTheme.colorScheme.onSurface
                )
           }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Locations",
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1.5f))
        }
        OutlinedTextField(
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            value = locationValue.value,
            onValueChange = {
                locationValue.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            prefix = {
                Image(
                    painterResource(id = R.drawable.ic_location_pin),
                    contentDescription = "Location Pin",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            maxLines = 1,
            placeholder = {
                Text(
                    text = "Search for a location",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            trailingIcon = {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurface,
                            strokeWidth = 1.5.dp
                        )
                    }
                } else  {
                    IconButton(
                        onClick = {
                            if (locationValue.value.isNotEmpty()) {
                                locationValue.value = ""
                            } else {
                                focusManager.clearFocus()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun LocationScreenDialog(
    title: String,
    subtitle: String,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave()
                    onDismiss()
                }
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}