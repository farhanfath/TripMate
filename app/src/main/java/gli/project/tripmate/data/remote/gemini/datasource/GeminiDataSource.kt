package gli.project.tripmate.data.remote.gemini.datasource

import gli.project.tripmate.data.remote.gemini.model.ChatRequest
import gli.project.tripmate.data.remote.gemini.model.ChatResponse

interface GeminiDataSource {
    suspend fun getChatResponse(message: ChatRequest) : ChatResponse
}