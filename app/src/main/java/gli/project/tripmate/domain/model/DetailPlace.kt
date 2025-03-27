package gli.project.tripmate.domain.model

data class DetailPlace(
    val placeId: String,
    val name: String,
    val country: String,
    val state: String,
    val city: String,
    val address: String,
    val openingHours: String,
    val websiteUrl: String,

    // for google map
    val lat: Double,
    val lon: Double
)