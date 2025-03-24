package gli.project.tripmate.presentation.ui.state

import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.util.ResultResponse

data class PlacesState(
    val nearbyPlaces: ResultResponse<List<Place>> = ResultResponse.Loading,
    val nearbyPlacesByCategory: ResultResponse<List<Place>> = ResultResponse.Loading,
    val detailPlace: ResultResponse<DetailPlace> = ResultResponse.Loading,
    val placeRange : Double = 0.0,
    val detailImage: ResultResponse<PexelImage> = ResultResponse.Loading
)