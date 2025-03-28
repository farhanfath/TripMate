package gli.project.tripmate.data.local.datasource

import androidx.paging.PagingSource
import gli.project.tripmate.data.local.dao.FavoriteDao
import gli.project.tripmate.data.local.model.FavoriteEntity
import javax.inject.Inject

class FavoriteDataSourceImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteDataSource {
    override suspend fun addFavorite(place: FavoriteEntity) = favoriteDao.addFavorite(place)

    override suspend fun removeFavorite(place: FavoriteEntity) = favoriteDao.removeFavorite(place)

    override suspend fun removeFavoriteById(id: Int) = favoriteDao.removeFavoriteById(id)

    override fun getAllFavorites(): PagingSource<Int, FavoriteEntity> =favoriteDao.getAllFavorites()

    override suspend fun isFavorite(id: Int): Boolean = favoriteDao.isFavorite(id)
}