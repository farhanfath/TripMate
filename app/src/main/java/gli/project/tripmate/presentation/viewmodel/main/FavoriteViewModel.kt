package gli.project.tripmate.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.domain.model.local.Favorite
import gli.project.tripmate.domain.usecase.FavoriteUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val useCase: FavoriteUseCase
) : ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun getFavoritePlaces() : Flow<PagingData<Favorite>> {
        return useCase.getAllFavorites()
            .map { result ->
                if (result is ResultResponse.Success) result.data else PagingData.empty()
            }
    }

    fun removeFavoriteById(favId: String) {
        viewModelScope.launch {
            useCase.removeFavoriteById(favId)
        }
    }

    fun checkFavoriteStatus(placeId: String) {
        viewModelScope.launch {
            _isFavorite.value = useCase.isFavorite(placeId)
        }
    }

    fun toggleFavorite(place: DetailPlace) {
        viewModelScope.launch {
            val currentStatus = useCase.isFavorite(place.placeId)
            if (currentStatus) {
                useCase.removeFavorite(place)
            } else {
                useCase.addFavorite(place)
            }
            _isFavorite.value = !currentStatus
        }
    }

}