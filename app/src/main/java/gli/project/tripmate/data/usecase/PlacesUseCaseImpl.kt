package gli.project.tripmate.data.usecase

import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.usecase.PlacesUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlacesUseCaseImpl @Inject constructor(
    private val repository: PlacesRepository
) : PlacesUseCase {
    override fun getNearbyPlaces(
        categories: String,
        filter: String,
        limit: Int
    ): Flow<ResultResponse<List<Place>>> {
        return repository.getNearbyPlaces(categories, filter, limit)
    }

    override suspend fun getDetailPlace(id: String): ResultResponse<DetailPlace> {
        return repository.getDetailPlace(id)
    }
}