package com.app.skycast.presentation.screen.aqi_info

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.skycast.domain.model.aqi.ForecastAqi
import com.app.skycast.domain.model.units.AirQuality
import com.app.skycast.presentation.ui.components.AppBar
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AqiInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: AqiInfoViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppBar(
                onBack = onBack,
                title = "AQI Info"
            )
        }
    ) { innerPadding->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ){
            if(state.forecastAqi != null){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item {
                        AqiInfoHeader(state.forecastAqi!!)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item {
                        Text(
                            text = "More details",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ParticulateItem(
                            particulate = "PM2.5",
                            value = "${state.forecastAqi?.currentDay?.pm2_5?.toInt() ?: "N/A"} µg/m³",
                            title = "Fine particles that can enter the lungs and bloodstream, causing heart and lung problems.",
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ParticulateItem(
                            particulate = "O₃",
                            value = "${state.forecastAqi?.currentDay?.pm10?.toInt() ?: "N/A"} µg/m³",
                            title = "A gas that forms at ground level, causing respiratory issues, especially in heat."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ParticulateItem(
                            particulate = "NO₂",
                            value = "${state.forecastAqi?.currentDay?.no2?.toInt() ?: "N/A"} µg/m³",
                            title = "A gas from vehicle and industrial emissions that can irritate airways and worsen breathing conditions."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ParticulateItem(
                            particulate = "SO₂",
                            value = "${state.forecastAqi?.currentDay?.so2?.toInt() ?: "N/A"} µg/m³",
                            title = "A gas produced by burning fossil fuels, causing lung irritation and contributing to acid rain."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ParticulateItem(
                            particulate = "CO",
                            value = "${state.forecastAqi?.currentDay?.co?.toInt() ?: "N/A"} µg/m³",
                            title = "A colorless, odorless gas that reduces oxygen levels in the body, leading to serious health risks."
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                        )
                        AqiInfoItem(
                            title = "Good (0-50)",
                            description = "The air quality is considered satisfactory in this category, with little to no risk to public health. The levels of air pollution are minimal, making it a safe environment for all activities. People can engage in outdoor activities without any health concerns.",
                            aqiColor = AirQuality.colorGradientList[0],
                            isSelected = state.forecastAqi?.currentDay?.value?.toInt() in 0..50,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AqiInfoItem(
                            title = "Moderate (51-100)",
                            description = "While the air quality remains generally acceptable, some pollutants may pose a concern for a small number of sensitive individuals. Those who are unusually sensitive to air pollution may experience mild symptoms, such as slight respiratory discomfort.",
                            aqiColor = AirQuality.colorGradientList[1],
                            isSelected = state.forecastAqi?.currentDay?.value?.toInt() in 51..100,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AqiInfoItem(
                            title = "Unhealthy for Sensitive Groups (101-150)",
                            description = "In this range, members of sensitive groups, such as children, the elderly, and individuals with pre-existing respiratory or heart conditions, may experience adverse health effects. The general public is less likely to be affected, but sensitive individuals should take precautions.",
                            aqiColor = AirQuality.colorGradientList[2],
                            isSelected = state.forecastAqi?.currentDay?.value?.toInt() in 101..150,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AqiInfoItem(
                            title = "Unhealthy (151-200)",
                            description = "Air quality in this category may begin to affect everyone, with sensitive groups experiencing more serious health effects. Common symptoms include throat irritation, respiratory discomfort, and fatigue. Individuals are encouraged to limit outdoor activities, especially those involving heavy exertion.",
                            aqiColor = AirQuality.colorGradientList[3],
                            isSelected = state.forecastAqi?.currentDay?.value?.toInt() in 151..200,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AqiInfoItem(
                            title = "Very Unhealthy (201-300)",
                            description = "At this level, the air quality poses a health alert, and everyone may experience more significant health effects. The risk of respiratory and cardiovascular symptoms increases for all individuals, not just those in sensitive groups",
                            aqiColor = AirQuality.colorGradientList[4],
                            isSelected = state.forecastAqi?.currentDay?.value?.toInt() in 201..300,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AqiInfoItem(
                            title = "Hazardous (301-500)",
                            description = "Air quality in this range is considered hazardous, and everyone may experience serious health effects. The entire population is likely to be affected, with symptoms including respiratory distress, chest pain, and severe fatigue.",
                            aqiColor = AirQuality.colorGradientList[5],
                            isSelected = state.forecastAqi?.currentDay?.value?.toInt() in 301..500,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun AqiInfoHeader(
    forecastAqi: ForecastAqi
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val markerRotation = remember { androidx.compose.animation.core.Animatable(170f) }

    // Calculate target rotation angle
    val targetRotation = 170f + (forecastAqi.currentDay.value / 500f) * 200f

    // Launch animation on composition
    LaunchedEffect(forecastAqi) {
        delay(500)
        markerRotation.animateTo(
            targetValue = targetRotation.toFloat(),
            animationSpec = spring(
                dampingRatio = 0.7f,
                stiffness = 100f,
            )
        )
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 32.dp),
            contentAlignment = Alignment.TopCenter
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .widthIn(max = 220.dp)
                    .height(230.dp)
                    .drawBehind {
                        val strokeWidth = 80f
                        val stroke = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                        )
                        val gradient = Brush.sweepGradient(
                            colorStops = arrayOf(
                                0.4f to AirQuality.colorGradientList[4],
                                0.5f to AirQuality.colorGradientList[0],
                                0.6f to AirQuality.colorGradientList[1],
                                0.7f to AirQuality.colorGradientList[2],
                                0.8f to AirQuality.colorGradientList[3],
                                1f to AirQuality.colorGradientList[4]
                            )
                        )
                        drawArc(
                            brush = gradient,
                            startAngle = 170f,
                            sweepAngle = 200f,
                            useCenter = false,
                            style = stroke,
                        )

                        // Calculate marker position
                        val currentAngle = markerRotation.value
                        val markerX =
                            size.width / 2 + (size.width / 2 - strokeWidth / 2) * cos(
                                Math.toRadians(
                                    currentAngle.toDouble()
                                )
                            ).toFloat()
                        val markerY =
                            size.height / 2 + (size.height / 2 - strokeWidth / 2) * sin(
                                Math.toRadians(
                                    currentAngle.toDouble()
                                )
                            ).toFloat()
                        val rotation = 90f + currentAngle

                        rotate(
                            degrees = rotation,
                            pivot = Offset(markerX, markerY)
                        ) {
                            drawLine(
                                color = textColor,
                                start = Offset(markerX, markerY + 20.dp.value),
                                end = Offset(markerX, markerY - 100.dp.value),
                                strokeWidth = 30f,
                                cap = StrokeCap.Round,
                            )
                        }
                    }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = forecastAqi.currentDay.value.toInt().toString(),
                fontSize = 80.dp.value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = forecastAqi.currentDay.toDescription(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = forecastAqi.currentDay.toLongDescription(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun AqiInfoItem(
    title: String,
    description: String,
    aqiColor: Color,
    isSelected: Boolean,
) {
    Column (
        modifier = Modifier
            .then(
                if(isSelected) Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
                else Modifier
            )
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(aqiColor)
                    .size(20.dp)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun ParticulateItem(
    particulate: String,
    value: String,
    title: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = particulate,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}