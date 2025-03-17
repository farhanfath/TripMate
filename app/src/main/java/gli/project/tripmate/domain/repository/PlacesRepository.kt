package gli.project.tripmate.domain.repository

import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    fun getNearbyPlaces(categories: String, filter: String, limit: Int) : Flow<ResultResponse<List<Place>>>

    suspend fun getDetailPlace(id: String) : ResultResponse<DetailPlace>
}