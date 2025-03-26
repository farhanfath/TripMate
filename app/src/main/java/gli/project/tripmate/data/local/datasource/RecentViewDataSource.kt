package gli.project.tripmate.data.local.datasource

import gli.project.tripmate.data.local.model.RecentViewEntity
import kotlinx.coroutines.flow.Flow

interface RecentViewDataSource {
    fun getAllRecentView(): Flow<List<RecentViewEntity>>
    suspend fun addRecentView(place: RecentViewEntity)
    suspend fun deleteOldestRecentView()
    suspend fun getRecentViewCount(): Int
    suspend fun isPlaceExists(placeId: String): Int
    suspend fun updateRecentView(recentView: RecentViewEntity)
}