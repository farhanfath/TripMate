package gli.project.tripmate.data.local.datasource

import gli.project.tripmate.data.local.RecentViewDao
import gli.project.tripmate.data.local.model.RecentViewEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentViewDataSourceImpl @Inject constructor(
    private val recentViewDao: RecentViewDao
) : RecentViewDataSource {
    override fun getAllRecentView(): Flow<List<RecentViewEntity>> {
        return recentViewDao.getAllRecentViews()
    }

    override suspend fun addRecentView(place: RecentViewEntity) {
        recentViewDao.addRecentView(place)
    }

    override suspend fun deleteOldestRecentView() {
        recentViewDao.deleteOldestRecentView()
    }

    override suspend fun getRecentViewCount(): Int {
        return recentViewDao.getRecentViewCount()
    }

    override suspend fun isPlaceExists(placeId: String): Int {
        return recentViewDao.isPlaceIdExists(placeId)
    }

    override suspend fun updateRecentView(recentView: RecentViewEntity) {
        recentViewDao.updateRecentView(recentView)
    }
}