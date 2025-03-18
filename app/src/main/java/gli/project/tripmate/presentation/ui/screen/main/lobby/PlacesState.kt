package gli.project.tripmate.presentation.ui.screen.main.lobby

import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.util.ResultResponse

data class PlacesState(
    val nearbyPlaces: ResultResponse<List<Place>> = ResultResponse.Loading
)