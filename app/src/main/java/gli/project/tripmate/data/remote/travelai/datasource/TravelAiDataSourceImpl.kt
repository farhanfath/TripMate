package gli.project.tripmate.data.remote.travelai.datasource

import gli.project.tripmate.data.remote.n8n.N8nApiService
import gli.project.tripmate.data.remote.n8n.model.WebhookRequest
import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.data.remote.travelai.TravelAiApiService
import gli.project.tripmate.domain.model.n8n.type.InputType
import javax.inject.Inject

class TravelAiDataSourceImpl @Inject constructor(
    private val travelAiApiService: TravelAiApiService
) : TravelAiDataSource {
    override suspend fun sendTravelMessage(message: String, inputType: InputType): Result<WebhookResponse> {
        return try {
            val response = travelAiApiService.sendTravelMessage(WebhookRequest(System.currentTimeMillis().toString(), message, inputType))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}