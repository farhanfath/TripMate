package gli.project.tripmate.data.remote.geoapify.datasource

import androidx.paging.PagingSource
import gli.project.tripmate.data.remote.geoapify.GeoApiService
import gli.project.tripmate.data.remote.geoapify.model.DetailPlaceResponse
import gli.project.tripmate.data.remote.geoapify.model.PlacesResponse
import gli.project.tripmate.data.remote.geoapify.paging.GeoApifyPagingSource
import gli.project.tripmate.domain.model.Place
import javax.inject.Inject

class PlacesRemoteDataSourceImpl @Inject constructor(
    private val geoApiService: GeoApiService
) : PlacesRemoteDataSource {
    override fun getNearbyPlacesPagingSource(
        categories: String,
        latitude: Double,
        longitude: Double,
        radius: Int
    ): PagingSource<Int, Place> {
        return GeoApifyPagingSource(geoApiService, categories, longitude, latitude, radius)
    }

    override suspend fun getDetailPlace(id: String): DetailPlaceResponse {
        return geoApiService.getDetailPlace(id)
    }
}