package gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.component.common.BaseModalBottomSheet
import gli.project.tripmate.presentation.ui.component.common.DashedDivider

@Composable
fun MoreReviewBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    BaseModalBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        LazyColumn {
            items(10) { index ->
                ReviewCardItem()

                if (index < 9) {
                    DashedDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        dashWidth = 8.dp,
                        dashGap = 4.dp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewCardItem() {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UserName",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Feb 5, 2024",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            RatingBar(rating = 4.5f, maxRating = 5)
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ornare faucibus sollicitudin. Nulla id mattis erat, nec imperdiet odio. Morbi fermentum ac massa id finibus. Cras faucibus viverra metus ut pretium. Quisque nibh enim, vehicula id semper a, maximus at sem. In dui magna, sodales quis turpis sed, pellentesque porttitor sapien. Ut id scelerisque ipsum, et consectetur magna. Maecenas aliquet, elit id vulputate feugiat, leo dolor cursus turpis, vel venenatis leo nibh sed sapien. Nulla orci lectus, aliquam non nisi in, consectetur blandit nisi. Nullam augue sapien, vulputate ac elementum sit amet, imperdiet non nisl.",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = "Like",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Helpful (10)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 15.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Filled.Flag,
                    contentDescription = "Like",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Report",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}