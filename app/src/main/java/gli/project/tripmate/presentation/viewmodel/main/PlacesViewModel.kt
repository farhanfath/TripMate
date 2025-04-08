package gli.project.tripmate.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.data.helper.LocationDataStore
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.usecase.PlacesUseCase
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.state.main.PlacesState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val useCase: PlacesUseCase,
    private val locationDataStore: LocationDataStore
) : ViewModel() {
    private val _placesState = MutableStateFlow(PlacesState())
    val placesState = _placesState.asStateFlow()

    private val _searchTrigger = MutableSharedFlow<Pair<String, String>>(replay = 0)

    init {
        loadDefaultNearbyPlaces()
    }

    private fun loadDefaultNearbyPlaces() {
        viewModelScope.launch {
            locationDataStore.currentLocation.collect { location ->
                location?.let { (lat, lon) ->
                    _placesState.update { it.copy(
                        userLatitude = lat,
                        userLongitude = lon,
                        isLocationLoaded = true
                    ) }

                    val nearbyPlaces = getNearbyPlaces(
                        categories = "tourism",
                        latitude = lat,
                        longitude = lon,
                        radius = 5000
                    )
                    _placesState.update { it.copy(nearbyPlaces = nearbyPlaces) }
                }
            }
        }
    }

    private fun loadNearbyPlacesByCategory(category: String) {
        viewModelScope.launch {
            // Pastikan lokasi sudah tersedia
            val currentState = _placesState.value
            if (currentState.userLatitude != 0.0 && currentState.userLongitude != 0.0) {
                // Gunakan lokasi yang sudah tersimpan di state
                val nearbyPlacesByCategoryFlow = getNearbyPlaces(
                    categories = category,
                    latitude = currentState.userLatitude,
                    longitude = currentState.userLongitude,
                    radius = 5000
                )

                _placesState.update { it.copy(nearbyPlacesByCategory = nearbyPlacesByCategoryFlow) }
            } else {
                // Jika lokasi belum tersedia, tunggu lokasi
                locationDataStore.currentLocation.collect { location ->
                    location?.let { (lat, lon) ->
                        // Update lokasi pengguna di state jika belum
                        _placesState.update { it.copy(
                            userLatitude = lat,
                            userLongitude = lon,
                            isLocationLoaded = true
                        ) }

                        // Dapatkan nearby places berdasarkan kategori
                        val nearbyPlacesByCategoryFlow = getNearbyPlaces(
                            categories = category,
                            latitude = lat,
                            longitude = lon,
                            radius = 5000
                        )

                        _placesState.update { it.copy(nearbyPlacesByCategory = nearbyPlacesByCategoryFlow) }
                        return@collect
                    }
                }
            }
        }
    }

    fun getNearbyPlacesByCategory(category: String) {
        loadNearbyPlacesByCategory(category)
    }

    private fun getNearbyPlaces(categories: String, latitude: Double, longitude: Double, radius: Int): Flow<PagingData<Place>> {
        return useCase.getNearbyPlaces(categories, latitude, longitude, radius)
            .map { result ->
                if (result is ResultResponse.Success) result.data else PagingData.empty()
            }
            .cachedIn(viewModelScope)
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

    // get places search by area
    val searchResults = _searchTrigger
        .flatMapLatest { (query, category) ->
            _placesState.update { it.copy(isSearchStarted = true) }
            useCase.getPlacesByArea(query, category)
        }
        .map {
            if (it is ResultResponse.Success) it.data else PagingData.empty()
        }
        .cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _placesState.update { it.copy(searchQuery = query) }
    }

    fun setCategory(category: String) {
        _placesState.update { it.copy(category = category) }
    }

    fun triggerSearch() {
        val query = _placesState.value.searchQuery
        val category = _placesState.value.category

        if (query.isNotBlank()) {
            viewModelScope.launch {
                _searchTrigger.emit(query to category)
            }
        }
    }

    fun clearSearch() {
        _placesState.update { it.copy(searchQuery = "", isSearchStarted = false) }
    }
}