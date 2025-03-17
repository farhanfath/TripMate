package gli.project.tripmate.data.repository

import gli.project.tripmate.data.mapper.toDomain
import gli.project.tripmate.data.remote.datasource.PlacesRemoteDataSource
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.util.ErrorMessage
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val remoteDataSource: PlacesRemoteDataSource
) : PlacesRepository {
    override fun getNearbyPlaces(
        categories: String,
        filter: String,
        limit: Int
    ): Flow<ResultResponse<List<Place>>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = remoteDataSource.getNearbyPlaces(categories, filter, limit)
            val places = response.toDomain()
            emit(ResultResponse.Success(places))
        } catch (e: IOException) {
            emit(ResultResponse.Error(ErrorMessage.NETWORK_ERROR))
        } catch (e: HttpException) {
            emit(ResultResponse.Error(ErrorMessage.SERVER_ERROR))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getDetailPlace(id: String): ResultResponse<DetailPlace> {
        return try {
            val response = remoteDataSource.getDetailPlace(id)
            val detailPlaces = response.toDomain()
            ResultResponse.Success(detailPlaces)
        } catch (e: IOException) {
            ResultResponse.Error(ErrorMessage.NETWORK_ERROR)
        } catch (e: HttpException) {
            ResultResponse.Error(ErrorMessage.SERVER_ERROR)
        }
    }
}