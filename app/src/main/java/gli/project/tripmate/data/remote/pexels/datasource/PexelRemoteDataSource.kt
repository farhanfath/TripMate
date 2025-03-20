package gli.project.tripmate.data.remote.pexels.datasource

import androidx.paging.PagingSource
import gli.project.tripmate.domain.model.PexelImage

interface PexelRemoteDataSource {
    fun getPexelsImagePagingSource(query: String): PagingSource<Int, PexelImage>
}