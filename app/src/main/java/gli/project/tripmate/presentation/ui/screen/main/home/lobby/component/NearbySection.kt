package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import gli.project.tripmate.R
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.error.PagingFooterError
import gli.project.tripmate.presentation.ui.component.common.error.RowSectionError
import gli.project.tripmate.presentation.ui.component.main.SeeMoreCard
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.loading.NearbyPlacesLoadingItem
import gli.project.tripmate.presentation.util.ErrorMessageHelper
import gli.project.tripmate.presentation.util.extensions.emptyTextHandler
import gli.project.tripmate.presentation.util.extensions.getRating
import gli.project.tripmate.presentation.util.extensions.handlePagingAppendState
import gli.project.tripmate.presentation.util.extensions.handlePagingState

@Composable
fun Nearby(
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onAddRecentView: (place: Place) -> Unit,
    placeData: LazyPagingItems<Place>,
    onSeeMoreClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Discover Nearby",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    onSeeMoreClick()
                }
            )
        }
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            handlePagingState(
                items = placeData,
                onLoading = {
                    items(5) {
                        NearbyPlacesLoadingItem()
                    }
                },
                onSuccess = {
                    val maxPlacesToShow = minOf(5, placeData.itemCount)
                    items(
                        count = maxPlacesToShow,
                        key = { index ->
                            val place = placeData[index]
                            if (place != null) {
                                "place_${place.placeId}_$index"
                            } else {
                                "null_$index"
                            }
                        }
                    ) { index ->
                        placeData[index]?.let { place ->
                            NearbyItem(
                                onDetailClick = {
                                    // navigate to detail
                                    onDetailClick(place.placeId, place.name)
                                    // add the data to recent view
                                    onAddRecentView(place)
                                },
                                place = place
                            )
                        }
                    }
                    if (placeData.itemCount > 5) {
                        item(key = "see_more") {
                            SeeMoreCard(
                                modifier = Modifier
                                    .height(240.dp)
                                    .width(180.dp),
                                onSeeMoreClick = {
                                    onSeeMoreClick()
                                },
                            )
                        }
                    }

                    if (placeData.itemCount < 5) {
                        handlePagingAppendState(
                            items = placeData,
                            onLoading = {
                                item {
                                    NearbyPlacesLoadingItem()
                                }
                            },
                            onError = { error ->
                                item {
                                    PagingFooterError (
                                        modifier = Modifier
                                            .height(200.dp)
                                            .width(180.dp),
                                        error = error.message
                                            ?: "Error loading more movies",
                                        onRetry = { placeData.retry() }
                                    )
                                }
                            }
                        )
                    }
                },
                onError = { error ->
                    item {
                        RowSectionError(
                            message = ErrorMessageHelper.getThrowableErrorMessage(error, LocalContext.current),
                            onRetry = { placeData.retry() },
                            modifier = Modifier.fillParentMaxWidth()
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun NearbyItem(
    onDetailClick: () -> Unit,
    place: Place
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .height(240.dp) // Increased height to accommodate new elements
            .width(180.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(16.dp) // Slightly more rounded corners
                clip = true
                shadowElevation = 8f
            }
            .clickable {
                onDetailClick()
            }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(130.dp)
            ) {
                CustomImageLoader(
                    url = place.image,
                    modifier = Modifier
                        .height(130.dp)
                        .fillMaxWidth(),
                    scale = ContentScale.Crop
                )

                // Add a gradient overlay for better text readability if needed
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                ),
                                startY = 0f,
                                endY = 130f
                            )
                        )
                )

                // Rating badge in top-right corner
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = getRating(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Place name with animation on hover/press
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = emptyTextHandler(place.name, stringResource(id = R.string.name_not_available)),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.size(6.dp))

            // Location with improved styling
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = place.city,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            // Categories displayed as chips in a horizontal row
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Assuming place.categories is a list of categories
                items(place.categories.take(3) ?: emptyList()) { category ->
                    CategoryChip(category = category)
                }

                // If there are more categories than shown
                if ((place.categories.size ?: 0) > 3) {
                    item {
                        Text(
                            text = "+${(place.categories.size ?: 0) - 3}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}