package gli.project.tripmate.data.remote.geoapify.datasource

import gli.project.tripmate.data.remote.geoapify.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.geoapify.model.PlacesResponse

interface PlacesRemoteDataSource {
    suspend fun getNearbyPlaces(
        categories: String,
        filter: String,
        limit: Int
    ) : PlacesResponse

    suspend fun getDetailPlace(id: String) : DetailPlaceResponse
}