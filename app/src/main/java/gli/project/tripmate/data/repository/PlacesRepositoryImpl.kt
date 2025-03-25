package gli.project.tripmate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gli.project.tripmate.data.mapper.toDomain
import gli.project.tripmate.data.remote.geoapify.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.remote.pexels.datasource.PexelRemoteDataSource
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.util.ErrorMessage
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class PlacesRepositoryImpl @Inject constructor(
    private val geoRemoteDataSource: PlacesRemoteDataSource,
    private val pexelRemoteDataSource: PexelRemoteDataSource
) : PlacesRepository {
    override fun getNearbyPlaces(
        categories: String,
        latitude: Double,
        longitude: Double,
        radius: Int
    ): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                geoRemoteDataSource.getNearbyPlacesPagingSource(categories, latitude, longitude, radius)
            }
        ).flow
    }

    override suspend fun getDetailPlace(id: String): ResultResponse<DetailPlace> {
        return try {
            val response = geoRemoteDataSource.getDetailPlace(id)
            val detailPlaces = response.toDomain()
            ResultResponse.Success(detailPlaces)
        } catch (e: IOException) {
            ResultResponse.Error(ErrorMessage.NETWORK_ERROR)
        } catch (e: HttpException) {
            ResultResponse.Error(ErrorMessage.SERVER_ERROR)
        }
    }

    override fun getUserLocationAndPlaceRange(
        latPlace: Double,
        lonPlace: Double,
        latUser: Double,
        lonUser: Double
    ): Double {
        val r = 6371.0
        val dLat = Math.toRadians(latUser - latPlace)
        val dLon = Math.toRadians(lonUser - lonPlace)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latPlace)) * cos(Math.toRadians(latUser)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }

    override fun getPlacesDetailImageList(query: String): Flow<PagingData<PexelImage>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { pexelRemoteDataSource.getPexelsImagePagingSource(query) }
        ).flow
    }

    override suspend fun getPlaceDetailBackgroundImage(query: String): ResultResponse<PexelImage> {
        return try {
            val response = pexelRemoteDataSource.getPexelDetailImage(query)
            val detailPlaces = response.toDomain()
            ResultResponse.Success(detailPlaces)
        } catch (e: IOException) {
            ResultResponse.Error(ErrorMessage.NETWORK_ERROR)
        } catch (e: HttpException) {
            ResultResponse.Error(ErrorMessage.SERVER_ERROR)
        }
    }
}