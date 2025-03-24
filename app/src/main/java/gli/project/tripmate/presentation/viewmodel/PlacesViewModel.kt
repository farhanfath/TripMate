package gli.project.tripmate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.data.helper.LocationDataStore
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.usecase.PlacesUseCase
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.ui.state.PlacesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val useCase: PlacesUseCase,
    private val locationDataStore: LocationDataStore
) : ViewModel() {
    private val _placesState = MutableStateFlow(PlacesState())
    val placesState = _placesState.asStateFlow()

    init {
        loadDefaultNearbyPlaces()
    }

    private fun loadDefaultNearbyPlaces() {
        viewModelScope.launch {
            locationDataStore.currentLocation.collect { location ->
                location?.let { (lat, lon) ->
                    fetchNearbyPlaces(
                        categories = "tourism",
                        filter = "circle:$lon,$lat,5000",
                        limit = 10,
                        isCategory = false
                    )
                }
            }
        }
    }

    private fun fetchNearbyPlaces(categories: String, filter: String, limit: Int, isCategory: Boolean) {
        viewModelScope.launch {
            useCase.getNearbyPlaces(categories, filter, limit)
                .onStart {
                    if (isCategory) {
                        _placesState.update { it.copy(nearbyPlacesByCategory = ResultResponse.Loading) }
                    } else {
                        _placesState.update { it.copy(nearbyPlaces = ResultResponse.Loading) }
                    }
                }
                .catch { e ->
                    if (isCategory) {
                        _placesState.update { it.copy(nearbyPlacesByCategory = ResultResponse.Error(e.message.toString())) }
                    } else {
                        _placesState.update { it.copy(nearbyPlaces = ResultResponse.Error(e.message.toString())) }
                    }
                }
                .collect { result ->
                    if (isCategory) {
                        _placesState.update { it.copy(nearbyPlacesByCategory = result) }
                    } else {
                        _placesState.update { it.copy(nearbyPlaces = result) }
                    }
                }
        }
    }

    fun getNearbyPlaces(categories: String, filter: String, limit: Int) {
        fetchNearbyPlaces(categories, filter, limit, isCategory = false)
    }

    fun getNearbyPlacesByCategory(categoryType: String) {
        viewModelScope.launch {
            locationDataStore.currentLocation.collectLatest { location ->
                location?.let { (lat, lon) ->
                    fetchNearbyPlaces(
                        categories = categoryType,
                        filter = "circle:$lon,$lat,10000",
                        limit = 20,
                        isCategory = true
                    )
                }
            }
        }
    }

    fun getPlaceRangeLocation(latPlace: Double, lonPlace: Double) {
        viewModelScope.launch {
            locationDataStore.currentLocation.collect { location ->
                location?.let { (latUser, lonUser) ->
                    val placeRange = useCase.getUserLocationAndPlaceRange(latPlace, lonPlace, latUser, lonUser)
                    _placesState.update { it.copy(placeRange = placeRange) }
                }
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

    fun getDetailListImage(query: String): Flow<PagingData<PexelImage>> {
        return useCase.getDetailPlaceImageList(query = query)
            .map { result ->
                if (result is ResultResponse.Success) result.data else PagingData.empty()
            }
            .cachedIn(viewModelScope)
    }

    fun getPexelDetailImage(query: String) {
        viewModelScope.launch {
            _placesState.update { it.copy(detailImage = ResultResponse.Loading) }
            try {
                val result = useCase.getPexelDetailImage(query)
                _placesState.update { it.copy(detailImage = result) }
            } catch (e: Exception) {
                _placesState.update { it.copy(detailImage = ResultResponse.Error(e.message.toString())) }
            }
        }
    }
}