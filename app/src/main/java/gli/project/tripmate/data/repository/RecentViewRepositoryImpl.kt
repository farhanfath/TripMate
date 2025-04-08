package gli.project.tripmate.data.repository

import gli.project.tripmate.data.local.datasource.RecentViewDataSource
import gli.project.tripmate.data.mapper.toDomain
import gli.project.tripmate.data.mapper.toEntity
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.RecentView
import gli.project.tripmate.domain.repository.RecentViewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentViewRepositoryImpl @Inject constructor(
    private val recentViewDataSource: RecentViewDataSource
) : RecentViewRepository {
    override suspend fun addRecentView(place: Place) {
        val exists = recentViewDataSource.isPlaceExists(place.placeId)
        if (exists > 0) return

        val count = recentViewDataSource.getRecentViewCount()
        if (count > 10) {
            recentViewDataSource.deleteOldestRecentView()
        }

        recentViewDataSource.addRecentView(place = place.toEntity())
    }

    override fun getAllRecentView(): Flow<List<RecentView>> {
        return recentViewDataSource.getAllRecentView().map { recentViewList ->
            recentViewList.map { it.toDomain() }
        }
    }

    override suspend fun updateRecentViewTimestamp(recentView: RecentView) {
        recentViewDataSource.updateRecentView(recentView = recentView.toEntity())
    }
}