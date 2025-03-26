package gli.project.tripmate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.RecentView
import gli.project.tripmate.domain.usecase.RecentViewUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewViewModel @Inject constructor(
    private val recentViewUseCase: RecentViewUseCase
) : ViewModel() {
    val recentView = recentViewUseCase.getAllRecentView()
        .map { result ->
            if (result is ResultResponse.Success) result.data.sortedByDescending { it.timeStamp } else emptyList()
        }

    fun addRecentView(place: Place) {
        viewModelScope.launch {
            recentViewUseCase.addRecentView(place)
        }
    }

    fun updateRecentViewTimeStamp(recentView: RecentView) {
        viewModelScope.launch {
            recentViewUseCase.updateRecentViewTimestamp(
                recentView = recentView.copy(
                    timeStamp = System.currentTimeMillis()
                )
            )
        }
    }
}