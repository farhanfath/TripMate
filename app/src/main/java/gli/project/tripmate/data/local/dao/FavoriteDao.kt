package gli.project.tripmate.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gli.project.tripmate.data.local.model.FavoriteEntity

@Dao
interface FavoriteDao {
    // favorite - database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(place: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(place: FavoriteEntity)

    @Query("DELETE FROM place_favorite WHERE id = :id")
    suspend fun removeFavoriteById(id: Int)

    @Query("SELECT * FROM place_favorite")
    fun getAllFavorites() : PagingSource<Int, FavoriteEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM place_favorite WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}