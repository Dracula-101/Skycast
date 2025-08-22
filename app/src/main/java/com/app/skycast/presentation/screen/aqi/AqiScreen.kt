package com.app.skycast.presentation.screen.aqi

import android.support.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.domain.util.precision
import com.app.skycast.presentation.ui.components.AppBar
import com.app.skycast.presentation.ui.components.LineChart
import com.app.skycast.presentation.util.debugOutline
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AqiScreen(
    modifier: Modifier = Modifier,
    viewModel: AqiViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scrollingState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                title = "Today's Air Quality",
                onBack = onBack
            )
        }
    ) { innerPadding->
        if(state.currentDayAqi !=null){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = scrollingState,
            ){
                item {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        CircularProgressDisplay(
                            value = state.currentDayAqi!!.value.toFloat() / 400f,
                            title = state.currentDayAqi!!.value.toInt().toString(),
                            radiusSize = 100.dp,
                            strokeWidth = 10.dp,
                            titleStyle = MaterialTheme.typography.headlineSmall,
                            color = state.currentDayAqi!!.toColor()
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = "Air Quality Index",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = state.currentDayAqi!!.toDescription(),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = state.currentDayAqi!!.toLongDescription(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    HorizontalDivider()
                }
                item {
                    Text(
                        text = "Pollutants",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    PollutantDisplay(
                        pollutant = "PM2.5",
                        value = state.currentDayAqi!!.getPM25Percentage().toFloat()/100f,
                        color = state.currentDayAqi!!.getPM25Color(),
                        unit = "µg/m³",
                        description = "Particulate matter less than 2.5 micrometers in diameter"
                    )
                    PollutantDisplay(
                        pollutant = "PM10",
                        value = state.currentDayAqi!!.getPM10Percentage().toFloat()/100f,
                        color = state.currentDayAqi!!.getPM10Color(),
                        unit = "µg/m³",
                        description = "Particulate matter less than 10 micrometers in diameter"
                    )
                    PollutantDisplay(
                        pollutant = "O₃",
                        value = state.currentDayAqi!!.getO3Percentage().toFloat()/100f,
                        color = state.currentDayAqi!!.getO3Color(),
                        unit = "ppb",
                        description = "Ozone is a gas composed of three oxygen atoms"
                    )
                    PollutantDisplay(
                        pollutant = "NO₂",
                        value = state.currentDayAqi!!.getNO2Percentage().toFloat()/100f,
                        color = state.currentDayAqi!!.getNO2Color(),
                        unit = "ppb",
                        description = "Nitrogen dioxide is a reddish-brown gas with a pungent odor"
                    )
                    PollutantDisplay(
                        pollutant = "SO₂",
                        value = state.currentDayAqi!!.getSO2Percentage().toFloat()/100f,
                        color = state.currentDayAqi!!.getSO2Color(),
                        unit = "ppb",
                        description = "Sulfur dioxide is a colorless gas with a pungent odor"
                    )
                    PollutantDisplay(
                        pollutant = "CO",
                        value = state.currentDayAqi!!.getCOPercentage().toFloat()/100f,
                        color = state.currentDayAqi!!.getCOColor(),
                        unit = "ppm",
                        description = "Carbon monoxide is a colorless, odorless gas"
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressDisplay(
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    color: Color = MaterialTheme.colorScheme.primary,
    radiusSize: Dp = 80.dp,
    strokeWidth: Dp = 8.dp,
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium
){
    val animatableValue = remember(value){ Animatable(0f) }
    LaunchedEffect(value){
        animatableValue.animateTo(
            value,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    Box(
        modifier = Modifier.size(radiusSize),
        contentAlignment = Alignment.Center,
    ){
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(radiusSize - strokeWidth / (2 *1.5f)),
            strokeWidth = strokeWidth / 1.5f,
            strokeCap = StrokeCap.Round,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
        CircularProgressIndicator(
            progress = { animatableValue.value },
            modifier = Modifier.size(radiusSize),
            strokeWidth = strokeWidth,
            color = color,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = title,
            modifier = Modifier.padding(8.dp),
            style = titleStyle
        )
    }
}

@Composable
fun PollutantDisplay(
    pollutant: String,
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    unit: String,
    color: Color,
    description: String
){
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CircularProgressDisplay(
            value = value,
            title = pollutant,
            radiusSize = 56.dp,
            strokeWidth = 4.dp,
            titleStyle = MaterialTheme.typography.bodySmall,
            color = color,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "${value.precision(2)} ($unit)",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}