package gli.project.tripmate.data.usecase

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.Favorite
import gli.project.tripmate.domain.repository.FavoriteRepository
import gli.project.tripmate.domain.usecase.FavoriteUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoriteRepository
) : FavoriteUseCase {
    override suspend fun addFavorite(place: DetailPlace) = repository.addFavorite(place)

    override suspend fun removeFavorite(place: DetailPlace) = repository.removeFavorite(place)

    override suspend fun removeFavoriteById(favId: String) = repository.removeFavoriteById(favId)

    override fun getAllFavorites(): Flow<ResultResponse<PagingData<Favorite>>> = flow {
        emit(ResultResponse.Loading)
        try {
            val favorites = repository.getAllFavorites()
            emitAll(favorites.map { ResultResponse.Success(it) })
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun isFavorite(favId: String): Boolean = repository.isFavorite(favId)
}