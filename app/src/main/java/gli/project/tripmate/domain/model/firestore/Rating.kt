package gli.project.tripmate.domain.model.firestore

data class Rating(
    val ratingId: String = "",
    val placeId: String = "",
    val userId: String = "",
    val userName: String = "",
    val description: String = "",
    val ratingValue: Double = 0.0,
    val likeCount: Int = 0,
    val timestamp: Long = 0L,
    val likedByUsers: List<String> = listOf() // List userId yang menyukai rating ini
)