package gli.project.tripmate.data.usecase

import gli.project.tripmate.domain.model.LocationModel
import gli.project.tripmate.domain.repository.LocationRepository
import gli.project.tripmate.domain.usecase.LocationUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
) : LocationUseCase {
    override suspend fun getCurrentLocation(): Flow<ResultResponse<LocationModel>> {
        return locationRepository.getCurrentLocation()
    }

    override fun isLocationServiceEnabled(): Boolean {
        return locationRepository.isLocationServiceEnabled()
    }

    override fun requestEnableLocationService() {
        locationRepository.requestEnableLocationService()
    }
}