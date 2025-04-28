package gli.project.tripmate.domain.model

data class Place(
    val placeId: String,
    val name: String = "",
    val country: String,
    val city: String,
    val image: String,
    // for range from nearest
    val lat: Double,
    val lon: Double,
    val categories: List<String>
)
