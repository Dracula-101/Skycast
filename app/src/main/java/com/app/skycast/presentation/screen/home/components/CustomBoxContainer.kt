package com.app.skycast.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp


@Composable
fun CustomBoxContainer(
    modifier: Modifier = Modifier,
    showPadding: Boolean = true,
    content:  @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                8.dp,
                shape = MaterialTheme.shapes.medium,
                ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .then(modifier),
        verticalArrangement = Arrangement.SpaceEvenly,
    ){
        content()
    }
}