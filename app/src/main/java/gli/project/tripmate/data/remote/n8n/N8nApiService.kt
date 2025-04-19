package gli.project.tripmate.data.remote.n8n

import gli.project.tripmate.data.remote.n8n.model.WebhookRequest
import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.data.util.ApiEndpoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface N8nApiService {
    @POST(ApiEndpoint.N8N_WEBHOOK)
    suspend fun sendMessage(@Body request: WebhookRequest): Response<WebhookResponse>
}