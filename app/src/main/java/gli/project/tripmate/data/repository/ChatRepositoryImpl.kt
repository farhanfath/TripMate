package gli.project.tripmate.data.repository

import gli.project.tripmate.data.remote.gemini.datasource.GeminiDataSource
import gli.project.tripmate.data.remote.gemini.model.ChatRequest
import gli.project.tripmate.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val geminiDataSource: GeminiDataSource
) : ChatRepository {
    override suspend fun getChatResponse(message: String): String {
        return try {
            val request = ChatRequest(
                listOf(mapOf("role" to "user", "text" to message))
            )
            val response = geminiDataSource.getChatResponse(request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "no response"
        } catch (e: Exception) {
            "Error ${e.localizedMessage}"
        }
    }
}