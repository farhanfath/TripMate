package gli.project.tripmate.data.remote.n8n.datasource

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse

interface N8nDataSource {
    suspend fun sendMessage(message: String): Result<WebhookResponse>
}