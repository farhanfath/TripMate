package gli.project.tripmate.presentation.ui.screen.main.home.rating_collection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gli.project.tripmate.presentation.state.main.RatingsState
import gli.project.tripmate.presentation.ui.component.common.CustomShimmer
import gli.project.tripmate.presentation.ui.component.common.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.component.common.DashedDivider
import gli.project.tripmate.presentation.ui.component.common.empty.EmptyRatingsView
import gli.project.tripmate.presentation.ui.component.common.error.CustomNoConnectionPlaceholder
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component.ReviewCardItem
import gli.project.tripmate.presentation.ui.theme.padding_16
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_8
import gli.project.tripmate.presentation.ui.theme.size_1
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel
import gli.project.tripmate.presentation.viewmodel.main.RatingViewModel

@Composable
fun RatingCollectionScreen(
    onBackClick: () -> Unit
) {
    val ratingViewModel = hiltViewModel<RatingViewModel>()
    val ratingState by ratingViewModel.ratingsState.collectAsState()

    val userViewModel = hiltViewModel<UserViewModel>()
    val userState by userViewModel.authState.collectAsState()
    val currentUserId = userState.currentUser?.id ?: ""

    LaunchedEffect(currentUserId) {
        ratingViewModel.loadRatingsByUserId(currentUserId)
    }

    Scaffold(
        topBar = {
            CustomTopBarWithNavigation(
                title = "Rating Location History",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            when(ratingState) {
                is RatingsState.Loading -> {
                    items(10) {
                        CustomShimmer(
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                is RatingsState.Success -> {
                    val ratings = (ratingState as RatingsState.Success).ratings

                    if (ratings.isEmpty()) {
                        item {
                            EmptyRatingsView(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(16.dp),
                                desc = "You haven't rated any locations so far. Your ratings will appear here when you add them."
                            )
                        }
                    } else {
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
                }
                is RatingsState.Error -> {
                    item {
                        CustomNoConnectionPlaceholder(
                            onRetry = { ratingViewModel.loadRatingsByUserId(currentUserId) },
                            title = "Error Acquired",
                            desc = (ratingState as RatingsState.Error).message
                        )
                    }
                }
            }
        }

    }
}