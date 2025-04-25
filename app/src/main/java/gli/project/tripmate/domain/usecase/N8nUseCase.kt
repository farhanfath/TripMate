package gli.project.tripmate.domain.usecase

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.domain.model.n8n.type.InputType

interface N8nUseCase {
    suspend fun sendMessageToWebhook(message: String, inputType: InputType): Result<WebhookResponse>
    suspend fun sendTravelMessageToWebhook(message: String, inputType: InputType): Result<WebhookResponse>
}