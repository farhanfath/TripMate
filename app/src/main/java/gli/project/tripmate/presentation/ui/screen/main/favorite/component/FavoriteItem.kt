package gli.project.tripmate.presentation.ui.screen.main.favorite.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.R
import gli.project.tripmate.domain.model.local.Favorite
import gli.project.tripmate.presentation.ui.component.CustomImageLoader
import gli.project.tripmate.presentation.ui.screen.detail.component.tab.review.component.RatingBar
import gli.project.tripmate.presentation.util.extensions.emptyTextHandler
import kotlinx.coroutines.launch

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
                // show confirmation dialog
                showConfirmationDialog = true
                false
            } else {
                false
            }
        }
    )

    // confirmation dialog
    if (showConfirmationDialog) {
        DeleteConfirmationDialog(
            placeName = favPlace.placeName,
            onConfirm = {
                showConfirmationDialog = false
                onDelete()
            },
            onDismiss = {
                showConfirmationDialog = false
                // Reset posisi item ke Settled
                scope.launch {
                    dismissState.reset()
                }
            }
        )
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            DismissBackground(dismissState)
        }
    ) {
        FavoriteItemContent(
            onDetailClick = onDetailClick,
            favPlace = favPlace
        )
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
        title = { Text("Konfirmasi Hapus") },
        text = { Text("Apakah Anda yakin ingin menghapus \"$placeName\" dari favorit?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier
                .scale(scale)
                .padding(end = 20.dp)
                .size(26.dp)
        )
    }
}

@Composable
fun FavoriteItemContent(
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    favPlace: Favorite,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .height(120.dp)
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(10.dp)
                clip = true
                shadowElevation = 10f
            }
            .clickable {
                onDetailClick(favPlace.placeId, favPlace.placeName)
            }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    CustomImageLoader(
                        url = favPlace.placeImage,
                        modifier = Modifier
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxWidth(),
                        scale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = emptyTextHandler(favPlace.placeName, stringResource(id = R.string.name_not_available))
                        )
                    }

                    RatingBar(rating = 4.5f, maxRating = 5, starSize = 18.dp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(15.dp)
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = favPlace.location,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
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