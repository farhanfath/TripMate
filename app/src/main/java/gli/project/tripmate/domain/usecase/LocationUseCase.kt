package gli.project.tripmate.domain.usecase

import gli.project.tripmate.domain.model.LocationModel
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface LocationUseCase {
    suspend fun getCurrentLocation() : Flow<ResultResponse<LocationModel>>
    fun isLocationServiceEnabled() : Boolean
}