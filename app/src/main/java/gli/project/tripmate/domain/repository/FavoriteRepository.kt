package gli.project.tripmate.domain.repository

import androidx.paging.PagingData
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    // favorite
    suspend fun addFavorite(place: Place)
    suspend fun removeFavorite(place: Place)
    suspend fun removeFavoriteById(favId: Int)
    fun getAllFavorites(): Flow<PagingData<Favorite>>
    suspend fun isFavorite(favId: Int): Boolean
}