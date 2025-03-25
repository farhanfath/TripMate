package gli.project.tripmate.presentation.ui.screen.more

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.component.CustomShimmer
import gli.project.tripmate.presentation.ui.component.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.component.error.CustomNoConnectionPlaceholder
import gli.project.tripmate.presentation.ui.screen.category.component.NearbyPlaceLongItem
import gli.project.tripmate.presentation.util.ErrorMessageHelper
import gli.project.tripmate.presentation.util.extensions.handlePagingState
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@Composable
fun MoreNearbyScreen(
    placesViewModel: PlacesViewModel,
    onBackClick: () -> Unit,
    onDetailClick: (placeId: String, placeName: String) -> Unit
) {
    // place state handler
    val placesState by placesViewModel.placesState.collectAsState()
    val nearbyPlacesState = placesState.nearbyPlaces.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CustomTopBarWithNavigation(
                title = "Nearby Places",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            handlePagingState(
                items = nearbyPlacesState,
                onLoading = {
                    items(20) {
                        CustomShimmer(
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                                .height(200.dp)
                                .fillMaxWidth()
                        )
                    }
                },
                onSuccess = {
                    items(
                        count = nearbyPlacesState.itemCount,
                        key = { index ->
                            val place = nearbyPlacesState[index]
                            if (place != null) {
                                "place_${place.placeId}_$index"
                            } else {
                                "null_$index"
                            }
                        }
                    ) { index ->
                        // get image from different api
                        nearbyPlacesState[index]?.let { place ->
                            NearbyPlaceLongItem(
                                onDetailClick = {
                                    onDetailClick(place.placeId, place.name)
                                },
                                place = place,
                            )
                        }
                    }
                },
                onError = { errorStatus ->
                    item {
                        CustomNoConnectionPlaceholder(
                            onRetry = { nearbyPlacesState.retry() },
                            title = "Error Acquired",
                            desc = ErrorMessageHelper.getThrowableErrorMessage(errorStatus, LocalContext.current)
                        )
                    }
                }
            )
        }
    }
}