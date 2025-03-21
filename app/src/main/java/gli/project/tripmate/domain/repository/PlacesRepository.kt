package gli.project.tripmate.domain.repository

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    fun getNearbyPlaces(categories: String, filter: String, limit: Int) : Flow<ResultResponse<List<Place>>>

    suspend fun getDetailPlace(id: String) : ResultResponse<DetailPlace>

    fun getUserLocationAndPlaceRange(latPlace: Double, lonPlace: Double, latUser: Double, lonUser: Double) : Double

    fun getPlacesDetailImageList(query: String) : Flow<PagingData<PexelImage>>

    suspend fun getPlaceDetailBackgroundImage(query: String) : ResultResponse<PexelImage>
}