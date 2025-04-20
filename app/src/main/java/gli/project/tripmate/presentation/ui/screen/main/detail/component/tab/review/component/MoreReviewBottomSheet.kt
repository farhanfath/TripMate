package gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import gli.project.tripmate.domain.model.firestore.Rating
import gli.project.tripmate.presentation.state.main.RatingsState
import gli.project.tripmate.presentation.ui.component.common.BaseModalBottomSheet
import gli.project.tripmate.presentation.ui.component.common.DashedDivider
import gli.project.tripmate.presentation.ui.theme.padding_16
import gli.project.tripmate.presentation.ui.theme.padding_20
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_8
import gli.project.tripmate.presentation.ui.theme.size_1
import gli.project.tripmate.presentation.ui.theme.size_8
import gli.project.tripmate.presentation.util.extensions.formatToRecentTime

@Composable
fun MoreReviewBottomSheet(
    ratingState: RatingsState,
    isVisible: Boolean,
    onDismiss: () -> Unit

) {
    BaseModalBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        LazyColumn {
            when(ratingState) {
                is RatingsState.Loading -> {}
                is RatingsState.Success -> {
                    val ratings = ratingState.ratings
                    itemsIndexed(ratings) { index, rating ->
                        ReviewCardItem(
                            ratingData = rating,
                        )

                        if (index < ratings.lastIndex) {
                            DashedDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = padding_16),
                                thickness = size_1,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                dashWidth = padding_8,
                                dashGap = padding_4
                            )
                        }
                    }
                }
                is RatingsState.Error -> {}
            }
        }
    }
}

@Composable
fun ReviewCardItem(
    ratingData: Rating
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = padding_20, vertical = padding_20)
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ratingData.userName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = ratingData.timestamp.formatToRecentTime(context),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                )
            }
            Spacer(modifier = Modifier.size(padding_4))
            RatingBar(rating = ratingData.ratingValue, maxRating = 5)
            Spacer(modifier = Modifier.size(size_8))
            Text(
                text = ratingData.description,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                )
            )
        }
    }
}