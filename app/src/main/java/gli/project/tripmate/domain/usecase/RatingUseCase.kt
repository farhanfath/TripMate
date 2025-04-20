package gli.project.tripmate.domain.usecase

import gli.project.tripmate.domain.model.firestore.Rating

interface RatingUseCase {
    suspend fun addRating(rating: Rating, placeId: String): Result<Unit>
    suspend fun deleteRating(ratingId: String): Result<Unit>
    suspend fun getRatingsByPlaceId(placeId: String): Result<List<Rating>>
    suspend fun getAverageRatingByPlaceId(placeId: String): Result<Double>
    suspend fun toggleLikeRating(ratingId: String, userId: String): Result<Unit>
    suspend fun getRatingById(ratingId: String): Result<Rating>
    suspend fun getRatingsByUserId(userId: String): Result<List<Rating>>
}