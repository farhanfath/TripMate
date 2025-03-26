package gli.project.tripmate.domain.usecase

import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.RecentView
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface RecentViewUseCase {
    suspend fun addRecentView(place: Place)
    fun getAllRecentView() : Flow<ResultResponse<List<RecentView>>>
    suspend fun updateRecentViewTimestamp(recentView: RecentView)
}