package gli.project.tripmate.domain.repository

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.domain.model.n8n.type.InputType

interface N8nRepository {
    suspend fun sendMessageToWebhook(message: String, inputType: InputType): Result<WebhookResponse>
}