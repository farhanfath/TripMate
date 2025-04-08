package gli.project.tripmate.data.remote.geoapify.datasource

import androidx.paging.PagingSource
import gli.project.tripmate.data.remote.geoapify.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.geoapify.model.PlacesResponse
import gli.project.tripmate.domain.model.Place

interface PlacesRemoteDataSource {
    fun getNearbyPlacesPagingSource(categories: String, latitude: Double, longitude: Double, radius: Int) : PagingSource<Int, Place>

    suspend fun getDetailPlace(id: String) : DetailPlaceResponse

    suspend fun getCoordinatesByArea(text: String) : PlacesResponse
}