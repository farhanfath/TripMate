package gli.project.tripmate.domain.repository

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse

interface N8nRepository {
    suspend fun sendMessageToWebhook(message: String): Result<WebhookResponse>
}