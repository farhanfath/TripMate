package gli.project.tripmate.domain.model.n8n

import gli.project.tripmate.domain.model.n8n.type.InputType
import gli.project.tripmate.domain.model.n8n.type.N8nType

data class ConversationItem(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val travelSpots: List<TravelSpot>? = null,
    val locationMap: LocationMap? = null,
    val actionFeature: FeatureAction? = null,
    val type: N8nType = N8nType.TEXT,
    val inputType: InputType = InputType.TEXT
)
