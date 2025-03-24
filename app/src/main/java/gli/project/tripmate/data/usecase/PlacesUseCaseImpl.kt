package gli.project.tripmate.data.usecase

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.usecase.PlacesUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlacesUseCaseImpl @Inject constructor(
    private val repository: PlacesRepository
) : PlacesUseCase {
    override fun getNearbyPlaces(
        categories: String,
        latitude: Double,
        longitude: Double,
        radius: Int
    ): Flow<ResultResponse<PagingData<Place>>>  = flow {
        emit(ResultResponse.Loading)
        try {
            val places = repository.getNearbyPlaces(categories, latitude, longitude, radius)
            emitAll(places.map { ResultResponse.Success(it) })
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getDetailPlace(id: String): ResultResponse<DetailPlace> {
        return repository.getDetailPlace(id)
    }

    override fun getUserLocationAndPlaceRange(
        latPlace: Double,
        lonPlace: Double,
        latUser: Double,
        lonUser: Double
    ): Double {
        return repository.getUserLocationAndPlaceRange(latPlace, lonPlace, latUser, lonUser)
    }

    override fun getDetailPlaceImageList(query: String): Flow<ResultResponse<PagingData<PexelImage>>> = flow {
        emit(ResultResponse.Loading)
        try {
            val images = repository.getPlacesDetailImageList(query)
            emitAll(images.map { ResultResponse.Success(it) })
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPexelDetailImage(query: String): ResultResponse<PexelImage> {
        return repository.getPlaceDetailBackgroundImage(query)
    }
}