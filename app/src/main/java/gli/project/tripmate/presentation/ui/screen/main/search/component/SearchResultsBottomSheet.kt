package gli.project.tripmate.presentation.ui.screen.main.search.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.component.common.BaseModalBottomSheet
import gli.project.tripmate.presentation.ui.component.common.CustomShimmer
import gli.project.tripmate.presentation.ui.screen.main.category.component.NearbyPlaceLongItem
import gli.project.tripmate.presentation.util.extensions.HandleComposePagingState
import gli.project.tripmate.presentation.util.extensions.handlePagingAppendState
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel

@Composable
fun ListSearchBottomSheet(
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
    placesViewModel: PlacesViewModel
) {
    val placesByArea = placesViewModel.searchResults.collectAsLazyPagingItems()

    BaseModalBottomSheet(
        modifier = Modifier.padding(top = 40.dp),
        isVisible = isVisible,
        onDismiss = onDismiss,
        fullScreen = true
    ) {
        HandleComposePagingState(
            items = placesByArea,
            onLoading = {
                LazyColumn {
                    items(10) {
                        CustomShimmer(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .height(200.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            },
            onError = {

            },
            onSuccess = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    items(
                        count = placesByArea.itemCount,
                        key = { index ->
                            val place = placesByArea[index]
                            if (place != null) {
                                "place_${place.placeId}_$index"
                            } else {
                                "null_$index"
                            }
                        }
                    ) { index ->
                        placesByArea[index]?.let { place ->
                            NearbyPlaceLongItem(
                                onDetailClick = {
                                    onDetailClick(place.placeId, place.name)
                                },
                                place = place
                            )
                        }
                    }

                    handlePagingAppendState(
                        items = placesByArea,
                        onLoading = {
                            item {
                                CustomShimmer(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .height(200.dp)
                                        .fillMaxWidth()
                                )
                            }
                        },
                        onError = {
                            item { Text("Terjadi error saat load awal") }
                        },
                        onNotLoading = {}
                    )
                }

            }
        )
    }
}