package gli.project.tripmate.data.remote

import gli.project.tripmate.data.remote.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.model.PlacesResponse
import gli.project.tripmate.data.util.ApiEndpoint
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(ApiEndpoint.NEARBY_PLACES)
    suspend fun getNearbyPlaces(
        @Query("categories") categories: String,
        @Query("filter") filter: String,
        @Query("limit") limit: Int,
    ) : PlacesResponse

    @GET(ApiEndpoint.DETAIL_PLACE)
    suspend fun getDetailPlace(
        @Query("id") id: String,
    ) : DetailPlaceResponse
}