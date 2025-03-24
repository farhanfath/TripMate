package gli.project.tripmate.data.remote.gemini.datasource

import gli.project.tripmate.domain.model.chatbot.ChatRequest
import gli.project.tripmate.domain.model.chatbot.ChatResponse

interface GeminiDataSource {
    suspend fun getChatResponse(message: ChatRequest) : ChatResponse
}