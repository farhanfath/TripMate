package gli.project.tripmate.presentation.ui.screen.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.component.CustomShimmer
import gli.project.tripmate.presentation.ui.component.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.screen.feature.component.NearbyPlaceByCategoryItem
import gli.project.tripmate.presentation.util.LogUtil
import gli.project.tripmate.presentation.util.extensions.HandlerResponseCompose
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

    LaunchedEffect(Unit) {
        placeViewModel.getNearbyPlacesByCategory(categoryType = categoryType)
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
            HandlerResponseCompose(
                response = placesCategoryState.nearbyPlacesByCategory,
                onLoading = {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        items(20) {
                            CustomShimmer(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 4.dp)
                                    .height(200.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                },
                onSuccess ={ placeList->
                    LazyColumn(
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(placeList) { place ->
                            // get image from different api
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
                    /**
                     * TODO: change to error handling
                     */
                    LogUtil.d("Nearby", "Error: $it")
                }
            )
        }
    }
}