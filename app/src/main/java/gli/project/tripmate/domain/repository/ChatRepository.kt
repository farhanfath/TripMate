package gli.project.tripmate.domain.repository

import gli.project.tripmate.domain.util.ResultResponse

interface ChatRepository {
    suspend fun getChatResponse(message: String): ResultResponse<String>
}