package gli.project.tripmate.data.usecase

import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.RecentView
import gli.project.tripmate.domain.repository.RecentViewRepository
import gli.project.tripmate.domain.usecase.RecentViewUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentViewUseCaseImpl @Inject constructor(
    private val recentViewRepository: RecentViewRepository
) : RecentViewUseCase {
    override suspend fun addRecentView(place: Place) {
        recentViewRepository.addRecentView(place)
    }

    override fun getAllRecentView(): Flow<ResultResponse<List<RecentView>>> = flow {
        emit(ResultResponse.Loading)
        try {
            val recentView = recentViewRepository.getAllRecentView()
            emitAll(recentView.map { ResultResponse.Success(it) })
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun updateRecentViewTimestamp(recentView: RecentView) {
        recentViewRepository.updateRecentViewTimestamp(recentView = recentView)
    }
}