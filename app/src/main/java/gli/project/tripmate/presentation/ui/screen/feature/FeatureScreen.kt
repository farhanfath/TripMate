package gli.project.tripmate.presentation.ui.screen.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.component.CustomShimmer
import gli.project.tripmate.presentation.ui.component.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.screen.feature.component.NearbyPlaceByCategoryItem
import gli.project.tripmate.presentation.util.extensions.handlePagingState
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@Composable
fun FeatureScreen(
    placeViewModel: PlacesViewModel,
    nameCategory: String,
    categoryType: String,
    onBackClick: () -> Unit,
    onDetailClick: (placeId: String, placeName: String) -> Unit
) {
    val placesCategoryState by placeViewModel.placesState.collectAsState()
    val nearbyPlacesCategoryState = placesCategoryState.nearbyPlacesByCategory.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        placeViewModel.getNearbyPlacesByCategory(category = categoryType)
    }

    Scaffold(
        topBar = {
            CustomTopBarWithNavigation(
                title = nameCategory,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                handlePagingState(
                    items = nearbyPlacesCategoryState,
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
                            count = nearbyPlacesCategoryState.itemCount,
                            key = { index ->
                                val place = nearbyPlacesCategoryState[index]
                                if (place != null) {
                                    "place_${place.placeId}_$index"
                                } else {
                                    "null_$index"
                                }
                            }
                        ) { index ->
                            // get image from different api
                            nearbyPlacesCategoryState[index]?.let { place ->
                                NearbyPlaceByCategoryItem(
                                    onDetailClick = {
                                        onDetailClick(place.placeId, place.name)
                                    },
                                    place = place,
                                )
                            }
                        }
                    },
                    onError = {
                        // TODO: Handle error
                    }
                )
            }
        }
    }
}