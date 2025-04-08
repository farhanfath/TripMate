package gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gli.project.tripmate.presentation.ui.component.common.DashedDivider
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component.MoreReviewBottomSheet
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component.RatingBarSummary
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component.ReviewSection
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_20

@Composable
fun ReviewTab() {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding_20, vertical = padding_10)
    ) {
        Text(
            text = "Review for this location",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(top = padding_20, bottom = padding_10)
        )
        RatingBarSummary()

        Spacer(modifier = Modifier.height(padding_10))

        DashedDivider(
            modifier = Modifier
                .padding(vertical = padding_10)
                .fillMaxWidth()
        )

        ReviewSection(
            onShowMoreReviews = { showBottomSheet = true }
        )
    }

    MoreReviewBottomSheet(
        isVisible = showBottomSheet,
        onDismiss = { showBottomSheet = false }
    )
}