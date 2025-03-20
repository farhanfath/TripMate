package gli.project.tripmate.data.remote.pexels.datasource

import androidx.paging.PagingSource
import gli.project.tripmate.data.remote.pexels.PexelsApiService
import gli.project.tripmate.data.remote.pexels.paging.PexelPagingSource
import gli.project.tripmate.domain.model.PexelImage
import javax.inject.Inject

class PexelRemoteDataSourceImpl @Inject constructor(
    private val pexelApiService: PexelsApiService
) : PexelRemoteDataSource {
    override fun getPexelsImagePagingSource(query: String): PagingSource<Int, PexelImage> {
        return PexelPagingSource(pexelApiService, query)
    }
}