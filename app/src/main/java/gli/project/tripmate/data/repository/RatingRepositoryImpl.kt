package gli.project.tripmate.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import gli.project.tripmate.domain.model.firestore.Rating
import gli.project.tripmate.domain.repository.RatingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RatingRepository {
    private val ratingsCollection = firestore.collection("ratings")

    override suspend fun addRating(rating: Rating, placeId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val ratingId = rating.ratingId.ifEmpty { ratingsCollection.document().id }
            val ratingWithId = rating.copy(ratingId = ratingId)

            ratingsCollection.document(ratingId).set(ratingWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteRating(ratingId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            ratingsCollection.document(ratingId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRatingsByPlaceId(placeId: String): Result<List<Rating>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = ratingsCollection
                .whereEqualTo("placeId", placeId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val ratings = snapshot.documents.mapNotNull { it.toObject(Rating::class.java) }
            Result.success(ratings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAverageRatingByPlaceId(placeId: String): Result<Double> = withContext(Dispatchers.IO) {
        try {
            val ratingsResult = getRatingsByPlaceId(placeId)

            if (ratingsResult.isFailure) {
                return@withContext Result.failure(ratingsResult.exceptionOrNull() ?: Exception("Unknown error"))
            }

            val ratings = ratingsResult.getOrNull() ?: return@withContext Result.success(0.0)

            if (ratings.isEmpty()) {
                return@withContext Result.success(0.0)
            }

            val averageRating = ratings.sumOf { it.ratingValue } / ratings.size
            Result.success(averageRating)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleLikeRating(ratingId: String, userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Menggunakan transaction untuk memastikan operasi atomic
            firestore.runTransaction { transaction ->
                val docRef = ratingsCollection.document(ratingId)
                val snapshot = transaction.get(docRef)
                val rating = snapshot.toObject(Rating::class.java) ?: throw Exception("Rating not found")

                val likedByUsers = rating.likedByUsers.toMutableList()
                val isAlreadyLiked = likedByUsers.contains(userId)

                if (isAlreadyLiked) {
                    likedByUsers.remove(userId)
                } else {
                    likedByUsers.add(userId)
                }

                transaction.update(docRef,
                    "likedByUsers", likedByUsers,
                    "likeCount", likedByUsers.size
                )
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRatingById(ratingId: String): Result<Rating> = withContext(Dispatchers.IO) {
        try {
            val snapshot = ratingsCollection.document(ratingId).get().await()
            val rating = snapshot.toObject(Rating::class.java) ?: throw Exception("Rating not found")
            Result.success(rating)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRatingsByUserId(userId: String): Result<List<Rating>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = ratingsCollection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val ratings = snapshot.documents.mapNotNull { it.toObject(Rating::class.java) }
            Result.success(ratings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}