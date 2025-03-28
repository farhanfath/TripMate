package gli.project.tripmate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import gli.project.tripmate.data.local.datasource.FavoriteDataSource
import gli.project.tripmate.data.mapper.toDomain
import gli.project.tripmate.data.mapper.toFavoriteEntity
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.Favorite
import gli.project.tripmate.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDataSource: FavoriteDataSource
) : FavoriteRepository {
    override suspend fun addFavorite(place: Place) {
        favoriteDataSource.addFavorite(place.toFavoriteEntity())
    }

    override suspend fun removeFavorite(place: Place) {
        favoriteDataSource.removeFavorite(place.toFavoriteEntity())
    }

    override suspend fun removeFavoriteById(favId: Int) {
        favoriteDataSource.removeFavoriteById(favId)
    }

    override fun getAllFavorites(): Flow<PagingData<Favorite>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { favoriteDataSource.getAllFavorites() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun isFavorite(favId: Int): Boolean = favoriteDataSource.isFavorite(favId)
}