package gli.project.tripmate.presentation.state.main

import gli.project.tripmate.domain.model.firestore.Rating

sealed class RatingsState {
    data object Loading : RatingsState()
    data class Success(val ratings: List<Rating>) : RatingsState()
    data class Error(val message: String) : RatingsState()
}