package gli.project.tripmate.presentation.ui.screen.main.category

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.CustomShimmer
import gli.project.tripmate.presentation.ui.component.common.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.component.common.error.CustomNoConnectionPlaceholder
import gli.project.tripmate.presentation.ui.screen.main.search.component.EnhancedNearbyPlaceItem
import gli.project.tripmate.presentation.util.ErrorMessageHelper
import gli.project.tripmate.presentation.util.extensions.handlePagingState
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.main.RecentViewViewModel

@Composable
fun CategoryScreen(
    placeViewModel: PlacesViewModel,
    recentViewViewModel: RecentViewViewModel,
    nameCategory: String,
    categoryType: String,
    onBackClick: () -> Unit,
    onDetailClick: (placeId: String, placeName: String) -> Unit
) {
    val placesCategoryState by placeViewModel.placesState.collectAsState()
    val nearbyPlacesCategoryState = placesCategoryState.nearbyPlacesByCategory.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        placeViewModel.getNearbyPlacesByCategory(category = categoryType)
    }

    Scaffold(
        topBar = {
            CustomTopBarWithNavigation(
                title = nameCategory,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 10.dp),
        ) {
            handlePagingState(
                items = nearbyPlacesCategoryState,
                onLoading = {
                    items(20) {
                        CustomShimmer(
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                                .height(200.dp)
                                .fillMaxWidth()
                        )
                    }
                },
                onSuccess = {
                    if (nearbyPlacesCategoryState.itemCount > 0) {
                        items(
                            count = nearbyPlacesCategoryState.itemCount,
                            key = { index ->
                                val place = nearbyPlacesCategoryState[index]
                                if (place != null) {
                                    "place_${place.placeId}_$index"
                                } else {
                                    "null_$index"
                                }
                            }
                        ) { index ->
                            // get image from different api
                            nearbyPlacesCategoryState[index]?.let { place ->
                                EnhancedNearbyPlaceItem(
                                    onDetailClick = {
                                        onDetailClick(place.placeId, place.name)
                                        recentViewViewModel.addRecentView(place)
                                    },
                                    place = place,
                                    index = index
                                )
                            }
                        }
                    } else {
                        item {
                            ElegantEmptyPlaceholder(
                                categoryName = nameCategory,
                                onRefresh = { nearbyPlacesCategoryState.refresh() },
                                onBackClick = onBackClick
                            )
                        }
                    }
                },
                onError = { errorStatus ->
                    item {
                        CustomNoConnectionPlaceholder(
                            onRetry = { nearbyPlacesCategoryState.retry() },
                            title = "Error Acquired",
                            desc = ErrorMessageHelper.getThrowableErrorMessage(errorStatus, LocalContext.current)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ElegantEmptyPlaceholder(
    categoryName: String,
    onRefresh: () -> Unit,
    onBackClick: () -> Unit
) {
    var isAnimating by remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(
        targetValue = if (isAnimating) 1.05f else 1f,
        animationSpec = repeatable(
            iterations = Int.MAX_VALUE,
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(key1 = true) {
        isAnimating = true
    }

    Box(
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomImageLoader(
                url = "https://img.freepik.com/premium-vector/lost-boy-holding-location-device-illustration-flat-style_67813-29227.jpg",
                modifier = Modifier
                    .size(200.dp)
                    .scale(animatedScale)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Belum ada destinasi",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Maaf, saat ini tidak ada tempat wisata untuk kategori '$categoryName' di sekitar Anda",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Interactive refresh button with elegant styling
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onRefresh() },
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    val rotation = remember { Animatable(0f) }

                    LaunchedEffect(key1 = isAnimating) {
                        if (isAnimating) {
                            while (true) {
                                rotation.animateTo(
                                    targetValue = 360f,
                                    animationSpec = tween(2000, easing = LinearEasing)
                                )
                                rotation.snapTo(0f)
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer { rotationZ = rotation.value }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Coba Lagi",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Jelajahi kategori lainnya",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        onBackClick()
                    }
            )
        }
    }
}