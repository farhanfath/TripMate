package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gli.project.tripmate.domain.model.local.RecentView
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_12
import gli.project.tripmate.presentation.ui.theme.padding_16
import gli.project.tripmate.presentation.ui.theme.padding_2
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.size_15
import gli.project.tripmate.presentation.ui.theme.size_280
import gli.project.tripmate.presentation.ui.theme.size_75
import gli.project.tripmate.presentation.ui.theme.size_80
import gli.project.tripmate.presentation.util.extensions.formatToRelativeTime
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
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(vertical = padding_10, horizontal = padding_4)
            .width(size_280)
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .width(280.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(10.dp)
                clip = true
                shadowElevation = 10f
            }
            .clickable {
                onDetailClick(recentViewItem.placeId, recentViewItem.placeName)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = padding_16, horizontal = padding_12)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(padding_10)
        ) {
            CustomImageLoader(
                url = recentViewItem.placeImage,
                modifier = Modifier
                    .size(size_80)
                    .clip(RoundedCornerShape(padding_10)),
                scale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .height(size_75),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = recentViewItem.placeName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = recentViewItem.timeStamp.formatToRelativeTime(context = LocalContext.current),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Thin,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(size_15)
                        )
                        Box(
                            modifier = Modifier.width(size_80)
                        ) {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = recentViewItem.location,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "",
                            tint = Color.Yellow,
                            modifier = Modifier.size(size_15)
                        )
                        Text(
                            text = "4.5",
                            modifier = Modifier.padding(horizontal = padding_2),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "(125)",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
