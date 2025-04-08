package gli.project.tripmate.data.remote.geoapify.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gli.project.tripmate.data.mapper.toDomain
import gli.project.tripmate.data.remote.geoapify.GeoApiService
import gli.project.tripmate.data.util.PagingError
import gli.project.tripmate.domain.model.Place
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GeoApifyPagingSource @Inject constructor(
    private val geoApiService: GeoApiService,
    private val categories: String,
    private val longitude: Double,
    private val latitude: Double,
    private val radius: Int
) : PagingSource <Int, Place>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Place> {
        val position = params.key ?: 0
        val limit = params.loadSize

        return try {
            val response = geoApiService.getNearbyPlaces(
                categories = categories,
                filter = "circle:$longitude,$latitude,$radius",
                limit = limit,
                offset = position
            )
            val places = response.features.map { it.toDomain() }
            LoadResult.Page(
                data = places,
                prevKey = if (position == 0) null else position - limit,
                nextKey = if (places.isEmpty()) null else position + limit
            )
        } catch (e: IOException) {
            LoadResult.Error(PagingError.NetworkError)
        } catch (e: HttpException) {
            LoadResult.Error(PagingError.ServerError)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Place>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}