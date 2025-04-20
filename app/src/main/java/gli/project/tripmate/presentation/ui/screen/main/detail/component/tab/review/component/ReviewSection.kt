package gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component

import android.util.Log
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
import gli.project.tripmate.domain.model.firestore.Rating
import gli.project.tripmate.presentation.state.main.RatingsState
import gli.project.tripmate.presentation.ui.component.common.empty.EmptyRatingsView
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.size_8

@Composable
fun ReviewSection(
    ratingState: RatingsState,
    onShowMoreReviews: () -> Unit) {
    when(ratingState) {
        is RatingsState.Loading -> {}
        is RatingsState.Success -> {
            if (ratingState.ratings.isEmpty()) {
                EmptyRatingsView(
                    desc = "This place hasn’t received any ratings yet. Be the first to share your experience!"
                )
            }
            ratingState.ratings.take(5).forEach { ratingData ->
                ReviewItem(
                    ratingData = ratingData
                )
            }

            if (ratingState.ratings.size >= 5) {
                OutlinedButton(
                    shape = RoundedCornerShape(padding_4),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onShowMoreReviews
                ) {
                    Text(
                        text = "see all review for this location"
                    )
                }
            }
        }
        is RatingsState.Error -> {
            Text(text = ratingState.message)
            Log.d("error", "error ${ratingState.message}")
        }
    }
}

@Composable
fun ReviewItem(
    ratingData: Rating
) {
    Column(
        modifier = Modifier
            .padding(vertical = padding_10)
            .fillMaxWidth()
    ) {
        Text(
            text = ratingData.userName,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.size(padding_4))
        RatingBar(rating = ratingData.ratingValue, maxRating = 5)
        Spacer(modifier = Modifier.size(size_8))
        Text(
            text = ratingData.description,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Justify
            )
        )
    }
}