package gli.project.tripmate.presentation.ui.screen.main.chat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gli.project.tripmate.R
import gli.project.tripmate.domain.model.n8n.TravelSpot
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader

@Composable
fun TravelSpotItem(
    travelSpot: TravelSpot,
    onClick: (TravelSpot) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(travelSpot) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image placeholder or actual image
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(70.dp)
            ) {
                CustomImageLoader(
                    url = "",
                    desc = travelSpot.name,
                    scale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = travelSpot.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = travelSpot.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB800),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        // ${travelSpot.reviewCount}
                        text = "${travelSpot.rating} (1k reviews)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "View Details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


//@Composable
//fun TravelSpotItem(
//    travelSpot: TravelSpot,
//    onClick: (TravelSpot) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clickable { onClick(travelSpot) }
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Placeholder image or actual image
//            CustomImageLoader(
//                url = "",
//                desc = "",
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = travelSpot.name,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = travelSpot.location,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = travelSpot.description,
//                    style = MaterialTheme.typography.bodySmall,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    color = Color.Gray
//                )
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            // Rating
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                RatingBar(rating = travelSpot.rating)
//                Text(
//                    text = "${travelSpot.rating}",
//                    style = MaterialTheme.typography.labelMedium
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun RatingBar(rating: Float, maxRating: Int = 5) {
//    Row {
//        repeat(maxRating) { index ->
//            val starRes = if (index < rating) {
//                R.drawable.ic_star_filled
//            } else if (index < rating + 0.5f && index >= rating) {
//                R.drawable.ic_star_half
//            } else {
//                R.drawable.ic_star_outline
//            }
//
//            Icon(
//                painter = painterResource(id = starRes),
//                contentDescription = "Star",
//                tint = Color(0xFFFFD700),
//                modifier = Modifier.size(16.dp)
//            )
//        }
//    }
//}