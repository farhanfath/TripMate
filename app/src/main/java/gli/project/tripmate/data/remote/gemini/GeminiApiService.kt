package gli.project.tripmate.data.remote.gemini

import gli.project.tripmate.data.remote.gemini.model.GeminiResponse
import gli.project.tripmate.domain.model.chatbot.ChatRequest
import gli.project.tripmate.domain.model.chatbot.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApiService {

    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun getChatResponse(
        @Body request: ChatRequest
    ) : GeminiResponse
}