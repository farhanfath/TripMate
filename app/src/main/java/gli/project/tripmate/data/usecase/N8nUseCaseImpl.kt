package gli.project.tripmate.data.usecase

import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.domain.model.n8n.type.InputType
import gli.project.tripmate.domain.repository.N8nRepository
import gli.project.tripmate.domain.usecase.N8nUseCase
import javax.inject.Inject

class N8nUseCaseImpl @Inject constructor(
    private val n8nRepository: N8nRepository
) : N8nUseCase {
    override suspend fun sendMessageToWebhook(message: String, inputType: InputType): Result<WebhookResponse> {
       return n8nRepository.sendMessageToWebhook(message, inputType)
    }

    override suspend fun sendTravelMessageToWebhook(
        message: String,
        inputType: InputType
    ): Result<WebhookResponse> {
        return n8nRepository.sendTravelMessageToWebhook(message, inputType)
    }
}