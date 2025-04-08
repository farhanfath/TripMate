package gli.project.tripmate.domain.usecase

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface PlacesUseCase {
    fun getNearbyPlaces(categories: String, latitude: Double, longitude: Double, radius: Int): Flow<ResultResponse<PagingData<Place>>>

    suspend fun getDetailPlace(id: String) : ResultResponse<DetailPlace>

    fun getUserLocationAndPlaceRange(latPlace: Double, lonPlace: Double, latUser: Double, lonUser: Double) : Double

    fun getDetailPlaceImageList(query: String): Flow<ResultResponse<PagingData<PexelImage>>>

    suspend fun getPexelDetailImage(query: String) : ResultResponse<PexelImage>

    fun getPlacesByArea(area: String, category: String) : Flow<ResultResponse<PagingData<Place>>>
}