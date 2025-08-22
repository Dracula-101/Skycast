package com.app.skycast.presentation.screen.home.components.forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ForecastChip(
    date: Date,
    temp: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .aspectRatio(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = SimpleDateFormat("h a", Locale.getDefault()).format(date),
            style = MaterialTheme.typography.bodySmall,
        )
        AsyncImage(
            model = icon,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(36.dp)
                .scale(1.1f)
        )
        Text(
            text = temp,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
