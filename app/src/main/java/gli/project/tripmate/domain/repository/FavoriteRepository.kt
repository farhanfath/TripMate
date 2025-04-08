package gli.project.tripmate.domain.repository

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.local.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    // favorite
    suspend fun addFavorite(place: DetailPlace)
    suspend fun removeFavorite(place: DetailPlace)
    suspend fun removeFavoriteById(favId: String)
    fun getAllFavorites(): Flow<PagingData<Favorite>>
    suspend fun isFavorite(favId: String): Boolean
}