package gli.project.tripmate.domain.usecase

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.local.Favorite
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface FavoriteUseCase {
    suspend fun addFavorite(place: DetailPlace)
    suspend fun removeFavorite(place: DetailPlace)
    suspend fun removeFavoriteById(favId: String)
    fun getAllFavorites(): Flow<ResultResponse<PagingData<Favorite>>>
    suspend fun isFavorite(favId: String): Boolean
}