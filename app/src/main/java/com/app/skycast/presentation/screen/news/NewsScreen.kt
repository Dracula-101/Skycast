package com.app.skycast.presentation.screen.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.app.skycast.domain.model.news.NewsArticle
import com.app.skycast.presentation.ui.components.AppBar
import com.app.skycast.presentation.ui.components.ShimmerEffect
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsScreen(
    onBack: () -> Unit,
    onNavigateToNewsDetails: (NewsArticle) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppBar(
                onBack = onBack,
                title = "News"
            )
        }
    ) { innerPadding->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            if(state.newsInfo == null) {
                NewsShimmerContent()
            } else {
                LazyColumn {
                    state.newsInfo?.articles?.forEach { newsArticle ->
                        item {
                            NewsItem(
                                newsArticle = newsArticle,
                                onClick = {
                                    onNavigateToNewsDetails(newsArticle)
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsShimmerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        repeat(5){
            Row(
                modifier = Modifier
                    .padding(16.dp),
            ){
                Column(
                    modifier = Modifier
                        .weight(3f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start,
                ){
                    ShimmerEffect(
                        modifier = Modifier.fillMaxWidth().height(25.dp)
                    )
                    ShimmerEffect(
                        modifier = Modifier.fillMaxWidth(0.6f).height(20.dp)
                    )
                    ShimmerEffect(
                        modifier = Modifier.fillMaxWidth(0.4f).height(15.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                )
            }
        }
    }
}

@Composable
fun NewsItem(
    newsArticle: NewsArticle,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = newsArticle.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = newsArticle.description,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(newsArticle.publishedAt),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = newsArticle.image,
                contentDescription = "News Image",
                modifier = Modifier
                    .width(100.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .aspectRatio(16/9f),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = newsArticle.source,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}