package gli.project.tripmate.domain.usecase

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse

interface N8nUseCase {
    suspend fun sendMessageToWebhook(message: String): Result<WebhookResponse>
}