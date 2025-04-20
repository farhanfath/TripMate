package gli.project.tripmate.data.repository

import gli.project.tripmate.data.remote.n8n.datasource.N8nDataSource
import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.domain.model.n8n.type.InputType
import gli.project.tripmate.domain.repository.N8nRepository
import javax.inject.Inject

class N8nRepositoryImpl @Inject constructor(
    private val n8nDataSource: N8nDataSource
) : N8nRepository {
    override suspend fun sendMessageToWebhook(message: String, inputType: InputType): Result<WebhookResponse> {
        return n8nDataSource.sendMessage(message, inputType)
    }
}