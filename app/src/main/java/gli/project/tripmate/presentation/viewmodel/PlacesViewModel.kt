package gli.project.tripmate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.usecase.PlacesUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val useCase: PlacesUseCase
) : ViewModel() {
    private val _nearbyState = MutableStateFlow<ResultResponse<List<Place>>>(ResultResponse.Loading)
    val nearbyState = _nearbyState.asStateFlow()

    init {
        getNearbyPlaces(
            categories = "tourism",
            filter = "circle:106.8456,-6.2088,5000",
            limit = 5
        )
    }

    fun getNearbyPlaces(categories: String, filter: String, limit: Int) {
        viewModelScope.launch {
            useCase.getNearbyPlaces(categories, filter, limit)
                .onStart {
                    _nearbyState.value = ResultResponse.Loading
                }
                .catch {
                    _nearbyState.value = ResultResponse.Error(it.message.toString())
                }
                .collect { result ->
                    _nearbyState.value = result
                }
        }
    }
}