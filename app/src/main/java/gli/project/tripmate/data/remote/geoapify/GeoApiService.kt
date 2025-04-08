package gli.project.tripmate.data.remote.geoapify

import gli.project.tripmate.data.remote.geoapify.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.geoapify.model.GeoCodeResponse
import gli.project.tripmate.data.remote.geoapify.model.PlacesResponse
import gli.project.tripmate.data.util.ApiEndpoint
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApiService {
    @GET(ApiEndpoint.NEARBY_PLACES)
    suspend fun getNearbyPlaces(
        @Query("categories") categories: String,
        @Query("filter") filter: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : PlacesResponse

    @GET(ApiEndpoint.DETAIL_PLACE)
    suspend fun getDetailPlace(
        @Query("id") id: String,
    ) : DetailPlaceResponse

//    @GET(ApiEndpoint.SEARCH_AUTOCOMPLETE)
//    suspend fun getSearchPlaces(
//        @Query("text") text: String
//    ) :

    @GET(ApiEndpoint.SEARCH)
    suspend fun getCoordinatesByArea(
        @Query("text") area: String
    ): PlacesResponse
}