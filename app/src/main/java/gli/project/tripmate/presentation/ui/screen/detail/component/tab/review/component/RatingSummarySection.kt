package gli.project.tripmate.presentation.ui.screen.detail.component.tab.review.component

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

@Composable
fun RatingBarSummary(
    rating: Float = 4.4f,
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
                    text = "${String.format("%.1f", rating)}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "/${maxRating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Star Rating
            RatingBar(rating = rating, maxRating = maxRating)

            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "Based on",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$totalReviews reviews",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        color = MaterialTheme.colorScheme.surfaceVariant,
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
                            "Good" -> MaterialTheme.colorScheme.tertiary
                            "Average" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun RatingBar(
    rating: Float,
    maxRating: Int = 5,
    starSize: Dp = 12.dp
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star
                else if (i <= rating + 0.5f) Icons.AutoMirrored.Filled.StarHalf
                else Icons.Filled.StarOutline,
                contentDescription = "Star $i",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(starSize)
            )
        }
    }
}