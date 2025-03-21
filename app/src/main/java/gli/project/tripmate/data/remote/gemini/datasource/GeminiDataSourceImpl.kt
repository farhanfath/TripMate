package gli.project.tripmate.data.remote.gemini.datasource

import gli.project.tripmate.data.remote.gemini.GeminiApiService
import gli.project.tripmate.data.remote.gemini.model.ChatRequest
import gli.project.tripmate.data.remote.gemini.model.ChatResponse
import javax.inject.Inject

class GeminiDataSourceImpl @Inject constructor(
    private val geminiApiService: GeminiApiService
) : GeminiDataSource {
    override suspend fun getChatResponse(message: ChatRequest): ChatResponse {
        return geminiApiService.getChatResponse(message)
    }
}