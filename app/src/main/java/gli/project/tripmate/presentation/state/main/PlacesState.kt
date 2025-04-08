package gli.project.tripmate.presentation.state.main

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.PlaceCategory
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.domain.util.constants.DataConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class PlacesState(
    val nearbyPlaces: Flow<PagingData<Place>> = flowOf(PagingData.empty()),
    val nearbyPlacesByCategory: Flow<PagingData<Place>> = flowOf(PagingData.empty()),
    val isLocationLoaded: Boolean = false,
    val detailPlace: ResultResponse<DetailPlace> = ResultResponse.Loading,
    val placeRange : Double = 0.0,
    val userLatitude : Double = 0.0,
    val userLongitude : Double = 0.0,
    val detailImage: ResultResponse<PexelImage> = ResultResponse.Loading,
    val placesCategory: List<PlaceCategory> = DataConstants.placeFilterCategories,
    val searchQuery: String = "",
    val category: String = "",
    val isSearchStarted: Boolean = false
)