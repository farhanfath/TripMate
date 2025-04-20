package gli.project.tripmate.data.usecase

import gli.project.tripmate.domain.model.firestore.Rating
import gli.project.tripmate.domain.repository.RatingRepository
import gli.project.tripmate.domain.usecase.RatingUseCase
import javax.inject.Inject

class RatingUseCaseImpl @Inject constructor(
    private val ratingRepository: RatingRepository
) : RatingUseCase{
    override suspend fun addRating(rating: Rating, placeId: String): Result<Unit> = ratingRepository.addRating(rating, placeId)

    override suspend fun deleteRating(ratingId: String): Result<Unit> = ratingRepository.deleteRating(ratingId)

    override suspend fun getRatingsByPlaceId(placeId: String): Result<List<Rating>> = ratingRepository.getRatingsByPlaceId(placeId)

    override suspend fun getAverageRatingByPlaceId(placeId: String): Result<Double> = ratingRepository.getAverageRatingByPlaceId(placeId)

    override suspend fun toggleLikeRating(ratingId: String, userId: String): Result<Unit> = ratingRepository.toggleLikeRating(ratingId, userId)

    override suspend fun getRatingById(ratingId: String): Result<Rating> = ratingRepository.getRatingById(ratingId)

    override suspend fun getRatingsByUserId(userId: String): Result<List<Rating>> = ratingRepository.getRatingsByUserId(userId)
}