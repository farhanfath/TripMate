package gli.project.tripmate.domain.usecase

import gli.project.tripmate.domain.util.ResultResponse

interface ChatUseCase {
    suspend fun getChatResponse(message: String) : ResultResponse<String>
}