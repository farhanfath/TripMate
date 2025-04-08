package gli.project.tripmate.domain.repository

import gli.project.tripmate.domain.model.LocationModel
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getCurrentLocation(): Flow<ResultResponse<LocationModel>>
    fun isLocationServiceEnabled(): Boolean
}