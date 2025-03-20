package gli.project.tripmate.data.remote.pexels

import gli.project.tripmate.data.remote.pexels.model.PexelResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {

    @GET("search")
    suspend fun getListImage(
        @Query("query") query: String,
        @Query("page") page: Int
    ) : PexelResponse
}