package gli.project.tripmate.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.model.firestore.Rating
import gli.project.tripmate.domain.usecase.RatingUseCase
import gli.project.tripmate.domain.usecase.UserUseCase
import gli.project.tripmate.presentation.state.main.RatingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val ratingUseCase: RatingUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _ratingsState = MutableStateFlow<RatingsState>(RatingsState.Loading)
    val ratingsState = _ratingsState.asStateFlow()

    // State untuk average rating
    private val _averageRating = MutableStateFlow(0.0)
    val averageRating = _averageRating.asStateFlow()

    // State untuk rating baru
    private val _newRatingDescription = MutableStateFlow("")
    val newRatingDescription = _newRatingDescription.asStateFlow()

    private val _newRatingValue = MutableStateFlow(0.0)
    val newRatingValue = _newRatingValue.asStateFlow()

    private val _ratingDistribution = MutableStateFlow<Map<String, Int>>(emptyMap())
    val ratingDistribution: StateFlow<Map<String, Int>> = _ratingDistribution

    // Fungsi untuk mengubah value rating baru
    fun updateNewRatingDescription(description: String) {
        _newRatingDescription.value = description
    }

    fun updateNewRatingValue(value: Double) {
        _newRatingValue.value = value
    }

    // Load ratings for a place
    fun loadRatings(placeId: String) {
        viewModelScope.launch {
            _ratingsState.value = RatingsState.Loading

            try {
                val result = ratingUseCase.getRatingsByPlaceId(placeId)
                if (result.isSuccess) {
                    _ratingsState.value = RatingsState.Success(result.getOrNull() ?: emptyList())
                } else {
                    _ratingsState.value = RatingsState.Error("Failed to load ratings: ${result.exceptionOrNull()?.message}")
                }

                // Load average rating
                loadAverageRating(placeId)

                ratingsState.collect { state ->
                    if (state is RatingsState.Success) {
                        calculateRatingDistribution(state.ratings)
                    }
                }
            } catch (e: Exception) {
                _ratingsState.value = RatingsState.Error("Failed to load ratings: ${e.message}")
            }
        }
    }

    fun loadRatingsByUserId(userId: String) {
        viewModelScope.launch {
            _ratingsState.value = RatingsState.Loading

            try {
                val result = ratingUseCase.getRatingsByUserId(userId)
                if (result.isSuccess) {
                    _ratingsState.value = RatingsState.Success(result.getOrNull() ?: emptyList())
                } else {
                    _ratingsState.value = RatingsState.Error("Failed to load ratings: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _ratingsState.value = RatingsState.Error("Failed to load ratings: ${e.message}")
            }
        }
    }

    // Load average rating for a place
    private fun loadAverageRating(placeId: String) {
        viewModelScope.launch {
            try {
                val result = ratingUseCase.getAverageRatingByPlaceId(placeId)
                if (result.isSuccess) {
                    _averageRating.value = result.getOrNull() ?: 0.0
                }
            } catch (e: Exception) {
                // Jika gagal mendapatkan rata-rata, tetap menggunakan nilai default
            }
        }
    }

    private fun calculateRatingDistribution(ratings: List<Rating>) {
        val distribution = mutableMapOf(
            "Excellent" to 0,
            "Very Good" to 0,
            "Good" to 0,
            "Average" to 0,
            "Poor" to 0
        )

        ratings.forEach { rating ->
            when {
                rating.ratingValue >= 4.5 -> distribution["Excellent"] = distribution["Excellent"]!! + 1
                rating.ratingValue >= 3.5 -> distribution["Very Good"] = distribution["Very Good"]!! + 1
                rating.ratingValue >= 2.5 -> distribution["Good"] = distribution["Good"]!! + 1
                rating.ratingValue >= 1.5 -> distribution["Average"] = distribution["Average"]!! + 1
                else -> distribution["Poor"] = distribution["Poor"]!! + 1
            }
        }

        _ratingDistribution.value = distribution
    }

    // Add a new rating
    fun addRating(placeId: String) {
        viewModelScope.launch {
            val currentUser = userUseCase.getCurrentUser()
            try {
                val rating = Rating(
                    placeId = placeId,
                    userId = currentUser?.id ?: "",
                    userName = currentUser?.userName ?: "Anonymous",
                    description = _newRatingDescription.value,
                    ratingValue = _newRatingValue.value,
                    likeCount = 0,
                    timestamp = System.currentTimeMillis()
                )

                val result = ratingUseCase.addRating(rating, placeId)

                if (result.isSuccess) {
                    // Clear form
                    _newRatingDescription.value = ""
                    _newRatingValue.value = 0.0

                    // Reload ratings
                    loadRatings(placeId)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Delete a rating
    fun deleteRating(ratingId: String, placeId: String) {
        viewModelScope.launch {
            try {
                val result = ratingUseCase.deleteRating(ratingId)

                if (result.isSuccess) {
                    // Reload ratings
                    loadRatings(placeId)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Toggle like on a rating
    fun toggleLike(ratingId: String, placeId: String) {

        viewModelScope.launch {
            val currentUser = userUseCase.getCurrentUser()

            try {
                val result = ratingUseCase.toggleLikeRating(
                    ratingId, currentUser
                        ?.id ?: ""
                )

                if (result.isSuccess) {
                    // Reload ratings
                    loadRatings(placeId)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}