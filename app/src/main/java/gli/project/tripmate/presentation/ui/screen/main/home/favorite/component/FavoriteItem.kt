package gli.project.tripmate.presentation.ui.screen.main.home.favorite.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.R
import gli.project.tripmate.domain.model.local.Favorite
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component.RatingBar
import gli.project.tripmate.presentation.util.extensions.emptyTextHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableFavoriteItem(
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onDelete: () -> Unit,
    favPlace: Favorite,
) {
    val scope = rememberCoroutineScope()
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { swipeActivationFloat -> swipeActivationFloat * 0.5f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showConfirmationDialog = true
                false
            } else {
                false
            }
        }
    )

    // Confirmation dialog
    if (showConfirmationDialog) {
        DeleteConfirmationDialog(
            placeName = favPlace.placeName,
            onConfirm = {
                showConfirmationDialog = false
                onDelete()
            },
            onDismiss = {
                showConfirmationDialog = false
                scope.launch {
                    dismissState.reset()
                }
            }
        )
    }

    // Animation for card entrance
    val animatedProgress = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(key1 = Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(300)
        )
    }

    val animatedModifier = Modifier
        .padding(horizontal = 16.dp)
        .graphicsLayer {
            alpha = animatedProgress.value
            translationY = (1f - animatedProgress.value) * 50f
        }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            DismissBackground(dismissState)
        },
        modifier = animatedModifier
    ) {
        FavoriteItemContent(
            onDetailClick = onDetailClick,
            favPlace = favPlace
        )
    }
}

@Composable
fun FavoriteGridItem(
    favPlace: Favorite,
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onDelete: () -> Unit
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // Animation for card entrance
    val animatedProgress = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(key1 = Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, delayMillis = 100)
        )
    }

    if (showConfirmationDialog) {
        DeleteConfirmationDialog(
            placeName = favPlace.placeName,
            onConfirm = {
                showConfirmationDialog = false
                onDelete()
            },
            onDismiss = {
                showConfirmationDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = animatedProgress.value
                scaleX = 0.8f + (0.2f * animatedProgress.value)
                scaleY = 0.8f + (0.2f * animatedProgress.value)
            }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clickable {
                    onDetailClick(favPlace.placeId, favPlace.placeName)
                },
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            shadowElevation = 4.dp
        ) {
            Box {
                // Image background
                CustomImageLoader(
                    url = favPlace.placeImage,
                    modifier = Modifier.fillMaxSize(),
                    scale = ContentScale.Crop
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 100f
                            )
                        )
                )

                // Content
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = emptyTextHandler(favPlace.placeName, stringResource(id = R.string.name_not_available)),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    RatingBar(rating = 4.5, maxRating = 5, starSize = 16.dp)

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )

                        Text(
                            text = favPlace.location,
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }

                // Delete icon in top-right corner
                IconButton(
                    onClick = { showConfirmationDialog = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    placeName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.DeleteForever, contentDescription = null) },
        title = {
            Text(
                "Konfirmasi Hapus",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                "Apakah Anda yakin ingin menghapus \"$placeName\" dari favorit?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
private fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val scale by animateFloatAsState(
        if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.8f else 1f
    )

    val color by animateColorAsState(
        if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
            MaterialTheme.colorScheme.errorContainer
        else
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier.padding(end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hapus",
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier
                    .scale(scale)
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun FavoriteItemContent(
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    favPlace: Favorite,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable {
                onDetailClick(favPlace.placeId, favPlace.placeName)
            },
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        Row {
            // Left image section
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight()
            ) {
                CustomImageLoader(
                    url = favPlace.placeImage,
                    modifier = Modifier.fillMaxSize(),
                    scale = ContentScale.Crop
                )

                // Category pill (could be dynamically assigned)
                val category = remember { listOf("Beach", "Mountain", "City", "Cultural").random() }
                Surface(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Right content section
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Place name with animation
                Text(
                    text = emptyTextHandler(favPlace.placeName, stringResource(id = R.string.name_not_available)),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RatingBar(rating = 4.5, maxRating = 5, starSize = 16.dp)
                    Text(
                        text = "4.5",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Location with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = favPlace.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Added time or other metadata
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Saved 3 days ago", // This would be dynamic based on your data
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }

                // View Details button
                OutlinedButton(
                    onClick = { onDetailClick(favPlace.placeId, favPlace.placeName) },
                    modifier = Modifier.align(Alignment.End),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Lihat Detail",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoriteItemPreview() {
    FavoriteItemContent(
        onDetailClick = { _, _ ->

        },
        favPlace = Favorite(
            favoriteId = "",
            placeId = "",
            placeName = "Jakarta Raya",
            placeImage = "",
            location = "Tangerang, Indonesia",
            timeStamp = 0
        )
    )
}