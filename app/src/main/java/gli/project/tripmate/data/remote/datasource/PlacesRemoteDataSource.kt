package gli.project.tripmate.data.remote.datasource

import gli.project.tripmate.data.remote.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.model.PlacesResponse

interface PlacesRemoteDataSource {
    suspend fun getNearbyPlaces(
        categories: String,
        filter: String,
        limit: Int
    ) : PlacesResponse

    suspend fun getDetailPlace(id: String) : DetailPlaceResponse
}