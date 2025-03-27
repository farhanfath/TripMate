package gli.project.tripmate.data.remote.datasource

import gli.project.tripmate.data.remote.ApiService
import gli.project.tripmate.data.remote.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.model.PlacesResponse
import javax.inject.Inject

class PlacesRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : PlacesRemoteDataSource {
    override suspend fun getNearbyPlaces(
        categories: String,
        filter: String,
        limit: Int
    ): PlacesResponse {
        return apiService.getNearbyPlaces(categories, filter, limit)
    }

    override suspend fun getDetailPlace(id: String): DetailPlaceResponse {
        return apiService.getDetailPlace(id)
    }
}