package gli.project.tripmate.data.remote.geoapify.datasource

import gli.project.tripmate.data.remote.geoapify.GeoApiService
import gli.project.tripmate.data.remote.geoapify.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.geoapify.model.PlacesResponse
import javax.inject.Inject

class PlacesRemoteDataSourceImpl @Inject constructor(
    private val geoApiService: GeoApiService
) : PlacesRemoteDataSource {
    override suspend fun getNearbyPlaces(
        categories: String,
        filter: String,
        limit: Int
    ): PlacesResponse {
        return geoApiService.getNearbyPlaces(categories, filter, limit)
    }

    override suspend fun getDetailPlace(id: String): DetailPlaceResponse {
        return geoApiService.getDetailPlace(id)
    }
}