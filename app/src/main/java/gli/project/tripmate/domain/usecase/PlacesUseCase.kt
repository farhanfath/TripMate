package gli.project.tripmate.domain.usecase

import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface PlacesUseCase {
    fun getNearbyPlaces(categories: String, filter: String, limit: Int) : Flow<ResultResponse<List<Place>>>

    suspend fun getDetailPlace(id: String) : ResultResponse<DetailPlace>
}