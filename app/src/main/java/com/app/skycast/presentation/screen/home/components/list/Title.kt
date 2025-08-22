package com.app.skycast.presentation.screen.home.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun LazyListScope.Title(
    title: String,
    showButton: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier? = null
) {
    item {
        Row(
            modifier = Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier ?: Modifier.padding(
                    top = 16.dp,
                    start = 16.dp,
                    bottom = 8.dp
                )
            )
            if (showButton){
                Spacer(modifier = Modifier.weight(1f))
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = onClick)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(start = 12.dp, end = 4.dp)
                        .padding(vertical = 2.dp)
                ){
                    Text(
                        text = "More",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
