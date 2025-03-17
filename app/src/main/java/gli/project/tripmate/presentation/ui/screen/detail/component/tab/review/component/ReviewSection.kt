package gli.project.tripmate.presentation.ui.screen.detail.component.tab.review.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ReviewSection(onShowMoreReviews: () -> Unit) {
    repeat(3) {
        ReviewItem()
    }

    OutlinedButton(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = onShowMoreReviews
    ) {
        Text(
            text = "see all review for this location"
        )
    }
}

@Composable
fun ReviewItem() {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "UserName",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.size(4.dp))
        RatingBar(rating = 4.5f, maxRating = 5)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ornare faucibus sollicitudin. Nulla id mattis erat, nec imperdiet odio. Morbi fermentum ac massa id finibus. Cras faucibus viverra metus ut pretium. Quisque nibh enim, vehicula id semper a, maximus at sem. In dui magna, sodales quis turpis sed, pellentesque porttitor sapien. Ut id scelerisque ipsum, et consectetur magna. Maecenas aliquet, elit id vulputate feugiat, leo dolor cursus turpis, vel venenatis leo nibh sed sapien. Nulla orci lectus, aliquam non nisi in, consectetur blandit nisi. Nullam augue sapien, vulputate ac elementum sit amet, imperdiet non nisl.",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Justify
            )
        )
    }
}