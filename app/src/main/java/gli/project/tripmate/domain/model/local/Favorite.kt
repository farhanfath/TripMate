package gli.project.tripmate.domain.model.local

data class Favorite(
    val favoriteId: String,
    val placeId: String,
    val placeName: String,
    val placeImage: String,
    val location: String,
    val timeStamp: Long
)