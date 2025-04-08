package gli.project.tripmate.data.local.datasource

import androidx.paging.PagingSource
import gli.project.tripmate.data.local.model.FavoriteEntity

interface FavoriteDataSource {
    suspend fun addFavorite(place: FavoriteEntity)
    suspend fun removeFavorite(place: FavoriteEntity)
    suspend fun removeFavoriteById(id: String)
    fun getAllFavorites(): PagingSource<Int, FavoriteEntity>
    suspend fun isFavorite(id: String): Boolean
}