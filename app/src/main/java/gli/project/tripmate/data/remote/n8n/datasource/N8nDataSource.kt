package gli.project.tripmate.data.remote.n8n.datasource

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.domain.model.n8n.type.InputType

interface N8nDataSource {
    suspend fun sendMessage(message: String, inputType: InputType): Result<WebhookResponse>
}