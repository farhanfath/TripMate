package gli.project.tripmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import gli.project.tripmate.data.local.model.RecentViewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentView(place: RecentViewEntity)

    @Query("SELECT * FROM recent_view ORDER BY id DESC LIMIT 10")
    fun getAllRecentViews() : Flow<List<RecentViewEntity>>

    @Query("DELETE FROM recent_view WHERE id IN (SELECT id FROM recent_view ORDER BY id ASC LIMIT 1)")
    suspend fun deleteOldestRecentView()

    @Query("SELECT COUNT(*) FROM recent_view")
    suspend fun getRecentViewCount(): Int

    @Query("SELECT COUNT(*) FROM recent_view WHERE placeId = :placeId")
    suspend fun isPlaceIdExists(placeId: String): Int

    @Update
    suspend fun updateRecentView(recentView: RecentViewEntity)
}