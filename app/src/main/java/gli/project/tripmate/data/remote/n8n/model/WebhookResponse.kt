package gli.project.tripmate.data.remote.n8n.model

import com.google.gson.annotations.SerializedName
import gli.project.tripmate.domain.model.n8n.FeatureAction
import gli.project.tripmate.domain.model.n8n.LocationMap
import gli.project.tripmate.domain.model.n8n.Product
import gli.project.tripmate.domain.model.n8n.TravelSpot
import gli.project.tripmate.domain.model.n8n.type.N8nType

data class WebhookResponse(
    val response: String,
    @SerializedName("travel_spots")
    val travelSpots: List<TravelSpot>? = null,
    @SerializedName("location_map")
    val locationMap: LocationMap? = null,
    @SerializedName("action_feature")
    val actionFeature: FeatureAction? = null,
    // rnd shopping assistant
    val searchQuery: String? = null,
    @SerializedName("product_list")
    val productList: List<Product>? = null,
    val responseType: N8nType = N8nType.TEXT
)

