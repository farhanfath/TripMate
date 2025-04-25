package gli.project.tripmate.data.remote.travelai.datasource

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.domain.model.n8n.type.InputType

interface TravelAiDataSource {
    suspend fun sendTravelMessage(message: String, inputType: InputType): Result<WebhookResponse>
}