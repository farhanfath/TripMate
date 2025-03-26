package gli.project.tripmate.domain.repository

import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.RecentView
import kotlinx.coroutines.flow.Flow

interface RecentViewRepository {
    suspend fun addRecentView(place: Place)
    fun getAllRecentView() : Flow<List<RecentView>>
    suspend fun updateRecentViewTimestamp(recentView: RecentView)
}