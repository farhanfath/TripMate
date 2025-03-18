package gli.project.tripmate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.usecase.PlacesUseCase
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.ui.state.PlacesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val useCase: PlacesUseCase
) : ViewModel() {
    private val _placesState = MutableStateFlow(PlacesState())
    val placesState = _placesState.asStateFlow()

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
                    _placesState.update { it.copy(nearbyPlaces = ResultResponse.Loading) }
                }
                .catch { e ->
                    _placesState.update { it.copy(nearbyPlaces = ResultResponse.Error(e.message.toString())) }
                }
                .collect { result ->
                    _placesState.update { it.copy(nearbyPlaces = result) }
                }
        }
    }

    fun getDetailPlaces(placeId: String) {
        viewModelScope.launch {
            _placesState.update { it.copy(detailPlace = ResultResponse.Loading) }
            try {
                val result = useCase.getDetailPlace(placeId)
                _placesState.update { it.copy(detailPlace = result) }
            } catch (e: Exception) {
                _placesState.update { it.copy(detailPlace = ResultResponse.Error(e.message.toString())) }
            }
        }
    }
}