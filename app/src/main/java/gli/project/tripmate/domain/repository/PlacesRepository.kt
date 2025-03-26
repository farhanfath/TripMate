package gli.project.tripmate.domain.repository

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    // location from geo api
    fun getNearbyPlaces(categories: String, latitude: Double, longitude: Double, radius: Int) : Flow<PagingData<Place>>
    suspend fun getDetailPlace(id: String) : ResultResponse<DetailPlace>

    // maps api + geo api
    fun getUserLocationAndPlaceRange(latPlace: Double, lonPlace: Double, latUser: Double, lonUser: Double) : Double

    // image with from pexels api
    fun getPlacesDetailImageList(query: String) : Flow<PagingData<PexelImage>>
    suspend fun getPlaceDetailBackgroundImage(query: String) : ResultResponse<PexelImage>
}