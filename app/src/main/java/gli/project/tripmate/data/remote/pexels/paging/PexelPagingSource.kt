package gli.project.tripmate.data.remote.pexels.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gli.project.tripmate.data.mapper.toDomain
import gli.project.tripmate.data.remote.pexels.PexelsApiService
import gli.project.tripmate.domain.model.PexelImage
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PexelPagingSource @Inject constructor(
    private val pexelApiService: PexelsApiService,
    private val query: String
) : PagingSource<Int, PexelImage>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PexelImage> {
        val page = params.key ?: 1
        return try {
            val response = pexelApiService.getListImage(query = query, page = page)
            LoadResult.Page(
                data = response.photos.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.photos.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(Throwable(e))
        } catch (e: HttpException) {
            LoadResult.Error(Throwable(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PexelImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}