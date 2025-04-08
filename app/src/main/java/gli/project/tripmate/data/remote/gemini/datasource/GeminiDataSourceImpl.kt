package gli.project.tripmate.data.remote.gemini.datasource

import gli.project.tripmate.data.mapper.toDomain
import gli.project.tripmate.data.remote.gemini.GeminiApiService
import gli.project.tripmate.domain.model.chatbot.ChatRequest
import gli.project.tripmate.domain.model.chatbot.ChatResponse
import javax.inject.Inject

class GeminiDataSourceImpl @Inject constructor(
    private val geminiApiService: GeminiApiService
) : GeminiDataSource {
    override suspend fun getChatResponse(message: ChatRequest): ChatResponse {
        return try {
            geminiApiService.getChatResponse(message).toDomain()
        } catch (e: Exception) {
            ChatResponse(emptyList())
        }
    }
}