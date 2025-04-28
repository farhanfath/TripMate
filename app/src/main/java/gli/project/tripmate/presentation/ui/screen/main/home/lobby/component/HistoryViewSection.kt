package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gli.project.tripmate.domain.model.local.RecentView
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_12
import gli.project.tripmate.presentation.ui.theme.padding_16
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.util.extensions.formatToRelativeTime
import gli.project.tripmate.presentation.util.extensions.formatToTimeAgo
import gli.project.tripmate.presentation.util.extensions.getRating
import gli.project.tripmate.presentation.viewmodel.main.RecentViewViewModel

@Composable
fun HistoryView(
    recentViewData : List<RecentView>,
    recentViewViewModel: RecentViewViewModel,
    onDetailClick: (placeId: String, placeName: String) -> Unit
) {
    if (recentViewData.isNotEmpty()) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recently Viewed",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(recentViewData) { recentViewItem ->
                    HistoryViewItem(
                        recentViewItem = recentViewItem,
                        onDetailClick = { placeId, placeName ->
                            onDetailClick(placeId, placeName)
                            recentViewViewModel.updateRecentViewTimeStamp(recentViewItem)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryViewItem(
    recentViewItem: RecentView,
    onDetailClick: (placeId: String, placeName: String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(vertical = padding_10, horizontal = padding_4)
            .width(280.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(16.dp)
                clip = true
                shadowElevation = if (isPressed) 4f else 8f
                scaleX = if (isPressed) 0.98f else 1f
                scaleY = if (isPressed) 0.98f else 1f
            }
            .clickable {
                onDetailClick(recentViewItem.placeId, recentViewItem.placeName)
            }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = padding_16, horizontal = padding_12)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(padding_10)
        ) {
            // Image with interactive effects
            Box(
                modifier = Modifier
                    .size(80.dp)
            ) {
                CustomImageLoader(
                    url = recentViewItem.placeImage,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 2.dp,
                            color = if (isPressed)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else
                                Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    scale = ContentScale.Crop
                )

                // Time badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(8.dp)
                        )
                        Text(
                            text = getRating(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .height(80.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = recentViewItem.placeName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Time viewed",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = recentViewItem.timeStamp.formatToRelativeTime(context = LocalContext.current),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }

                    // Show categories if available
                    if (recentViewItem.categories.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(recentViewItem.categories.take(2)) { category ->
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            modifier = Modifier
                                .width(150.dp)
                                .basicMarquee(),
                            text = recentViewItem.location,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
