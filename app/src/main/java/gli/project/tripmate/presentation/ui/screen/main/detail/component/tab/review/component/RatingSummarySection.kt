package gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.util.extensions.ratingFormat

@Composable
fun RatingBarSummary(
    rating: Double = 4.4,
    maxRating: Int = 5,
    totalReviews: Int = 20,
    ratingDistribution: Map<String, Int> = mapOf(
        "Excellent" to 295,
        "Very Good" to 123,
        "Good" to 51,
        "Average" to 19,
        "Poor" to 8
    )
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left section - Rating number and stars
        Column(
            modifier = Modifier.weight(0.25f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(bottom = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text =  ratingFormat(rating),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "/${maxRating}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Star Rating
            RatingBar(rating = rating, maxRating = maxRating)

            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "Based on",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "$totalReviews reviews",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Right section - Rating distribution
        Column(
            modifier = Modifier
                .weight(0.75f)
                .padding(start = 16.dp)
        ) {
            // Calculate the highest count for scaling
            val maxCount = ratingDistribution.values.maxOrNull() ?: 1

            ratingDistribution.entries.sortedByDescending {
                when(it.key) {
                    "Excellent" -> 5
                    "Very Good" -> 4
                    "Good" -> 3
                    "Average" -> 2
                    "Poor" -> 1
                    else -> 0
                }
            }.forEach { (label, count) ->
                RatingDistributionRow(
                    label = label,
                    count = count,
                    maxCount = maxCount
                )
            }
        }
    }
}

@Composable
fun RatingDistributionRow(
    label: String,
    count: Int,
    maxCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(80.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        // Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .height(12.dp)
        ) {
            // Background bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(3.dp)
                    )
                    .align(Alignment.CenterStart)
            )

            // Filled bar
            val fillWidth = if (maxCount > 0) count.toFloat() / maxCount else 0f
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillWidth)
                    .height(6.dp)
                    .background(
                        color = when(label) {
                            "Excellent" -> MaterialTheme.colorScheme.primary
                            "Very Good" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            "Good" -> MaterialTheme.colorScheme.secondary
                            "Average" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                            "Poor" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.primary
                        },
                        shape = RoundedCornerShape(3.dp)
                    )
                    .align(Alignment.CenterStart)
            )
        }

        // Count
        Text(
            text = "($count)",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun RatingBar(
    rating: Double,
    maxRating: Int = 5,
    starSize: Dp = 12.dp
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..maxRating) {
            val icon = when {
                i <= rating -> Icons.Filled.Star
                i <= rating + 0.5f -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(starSize)
            )
        }
    }
}