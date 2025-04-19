package gli.project.tripmate.domain.model.n8n

data class TravelSpot(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val rating: Float,
    val imageUrl: String? = null
)
