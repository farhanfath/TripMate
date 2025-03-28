package gli.project.tripmate.domain.usecase

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.Favorite
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface FavoriteUseCase {
    suspend fun addFavorite(place: Place)
    suspend fun removeFavorite(place: Place)
    suspend fun removeFavoriteById(favId: Int)
    fun getAllFavorites(): Flow<ResultResponse<PagingData<Favorite>>>
    suspend fun isFavorite(favId: Int): Boolean
}