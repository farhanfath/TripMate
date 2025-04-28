package gli.project.tripmate.domain.model.local

data class RecentView(
    val recentViewId: Int,
    val placeId: String,
    val placeName: String,
    val placeImage: String,
    val location: String,
    val timeStamp: Long,
    val categories: List<String>,
)
