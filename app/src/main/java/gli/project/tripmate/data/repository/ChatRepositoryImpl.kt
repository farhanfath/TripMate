package gli.project.tripmate.data.repository

import gli.project.tripmate.data.remote.gemini.datasource.GeminiDataSource
import gli.project.tripmate.data.remote.gemini.model.ChatContent
import gli.project.tripmate.data.remote.gemini.model.ChatPart
import gli.project.tripmate.data.remote.gemini.model.ChatRequest
import gli.project.tripmate.domain.repository.ChatRepository
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.util.LogUtil
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val geminiDataSource: GeminiDataSource
) : ChatRepository {
    override suspend fun getChatResponse(message: String): ResultResponse<String> {
        return try {
            val request = ChatRequest(
                contents = listOf(
                    ChatContent(
                        role = "user",
                        parts = listOf(
                            ChatPart(text = message)
                        )
                    )
                )
            )
            val response = geminiDataSource.getChatResponse(request)
            LogUtil.d("gemini tes", "cek status: $response")
            val result = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "no response"
            ResultResponse.Success(result)
        } catch (e: Exception) {
            ResultResponse.Error("Error ${e.localizedMessage}")
        }
    }
}